package com.stack.dogcat.gomall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.AttributeNameMapper;
import com.stack.dogcat.gomall.product.mapper.AttributeValueMapper;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.product.responseVo.ProductQueryResponseVo;
import com.stack.dogcat.gomall.product.requestVo.ProductSaveRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ProductUpdateRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ScreenProductsRequestVo;
import com.stack.dogcat.gomall.product.responseVo.ProductWithAttrbutes;
import com.stack.dogcat.gomall.product.responseVo.ProductWithStoreQueryResponseVo;
import com.stack.dogcat.gomall.product.responseVo.StoreQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IProductService;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    ProductMapper productMapper;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    AttributeNameMapper attributeNameMapper;

    @Autowired
    AttributeValueMapper attributeValueMapper;

    /**
     * 商家上架商品
     * @param requestVo
     */
    @Override
    @Transactional
    public void saveProduct(ProductSaveRequestVo requestVo) {

        LocalDateTime now = LocalDateTime.now();

        // 商品的最高、低价格
        BigDecimal highestPrice = BigDecimal.valueOf(0.0);
        BigDecimal lowestPrice = BigDecimal.valueOf(Double.MAX_VALUE);

        // 商品的库存
        Integer stockNum = 0;

        /**
         * 商品表
         */
        Product product = new Product();
        product.setDescription(requestVo.getDescription());
        product.setGmtCreate(now);
        product.setImagePath(requestVo.getImagePath());
        product.setName(requestVo.getName());
        product.setStoreId(requestVo.getStoreId());
        product.setTypeId(requestVo.getTypeId());
        product.setStockNum(0); //为满足非空约束，此处暂设为0，后续会更新为正确值
        productMapper.insert(product);

        // 获取刚刚插入的商品
        Product productDB = productMapper.selectById(product.getId());

        /**
         * sku表
         */
        //"{\"attrNameIdArray\":[1, 2],\"data\": [{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]},{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]}]}";
        String skusString = requestVo.getSkusString();
        JSONObject parse = JSON.parseObject(skusString);

        // 获取attrNameIdArray
        JSONArray attrNameIdJSONArray = parse.getJSONArray("attrNameIdArray");
        List<Integer> attrNameIdArray = attrNameIdJSONArray.toJavaList(Integer.class);
        List<String> attrNames = new ArrayList<>(); //属性名的name
        for (Integer id : attrNameIdArray) {
            attrNames.add(attributeNameMapper.selectById(id).getName());
        }

        // 获取data
        JSONArray dataJSONArray = parse.getJSONArray("data");
        List<ProductWithAttrbutes> data = dataJSONArray.toJavaList(ProductWithAttrbutes.class);

        for (ProductWithAttrbutes datum : data) {
            List<Integer> valueArray = datum.getValueArray();
            List<String> attrValues = new ArrayList<>(); //属性值的value
            for (Integer valueId : valueArray) {
                attrValues.add(attributeValueMapper.selectById(valueId).getValue());
            }

            Sku sku = new Sku();
            sku.setGmtCreate(now);
            sku.setPrice(datum.getPrice());
            sku.setProductId(productDB.getId());
            sku.setStockNum(datum.getStockNum());

            String productAttribute = "";
            productAttribute += "{";

            for (int i = 0; i < attrValues.size(); i++) {
                productAttribute += ("\"" + attrNames.get(i) + "\"" + ":" + "\"" + attrValues.get(i) + "\"");
                if(i != attrValues.size() - 1) {
                    productAttribute += ",";
                }
            }

            productAttribute += "}";

            sku.setProductAttribute(productAttribute);

            skuMapper.insert(sku);

            // 获取商品的最高、低价
            highestPrice = (datum.getPrice().compareTo(highestPrice) == 1)? datum.getPrice(): highestPrice;
            lowestPrice = (datum.getPrice().compareTo(lowestPrice) == -1)? datum.getPrice(): lowestPrice;

            // 获取商品库存
            stockNum += datum.getStockNum();

        }

        // 更新商品表以填入商品的最高、低价、库存
        productDB.setHighestPrice(highestPrice);
        productDB.setLowestPrice(lowestPrice);
        productDB.setStockNum(stockNum);
        productMapper.updateById(productDB);

    }

    /**
     * 商家下架商品
     * @param id
     */
    @Override
    public void deleteProductById(Integer id) {
        productMapper.deleteById(id);
    }

    /**
     * 商家修改商品信息
     * @param requestVo
     */
    @Override
    public void updateProducts(ProductUpdateRequestVo requestVo) {

        Product productDB = productMapper.selectById(requestVo.getProductId());

        LocalDateTime gmtCreate = productDB.getGmtCreate();

        // 商品的最高、低价格
        BigDecimal highestPrice = BigDecimal.valueOf(0.0);
        BigDecimal lowestPrice = BigDecimal.valueOf(Double.MAX_VALUE);

        // 商品的库存
        Integer stockNum = 0;

        /**
         * sku表
         */
        //先将该产品对应的sku删除，再重新添加新的sku信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        skuMapper.delete(queryWrapper);

        //"{\"attrNameIdArray\":[1, 2],\"data\": [{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]},{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]}]}";
        String skusString = requestVo.getSkusString();
        JSONObject parse = JSON.parseObject(skusString);

        // 获取attrNameIdArray
        JSONArray attrNameIdJSONArray = parse.getJSONArray("attrNameIdArray");
        List<Integer> attrNameIdArray = attrNameIdJSONArray.toJavaList(Integer.class);
        List<String> attrNames = new ArrayList<>(); //属性名的name
        for (Integer id : attrNameIdArray) {
            attrNames.add(attributeNameMapper.selectById(id).getName());
        }

        // 获取data
        JSONArray dataJSONArray = parse.getJSONArray("data");
        List<ProductWithAttrbutes> data = dataJSONArray.toJavaList(ProductWithAttrbutes.class);

        for (ProductWithAttrbutes datum : data) {
            List<Integer> valueArray = datum.getValueArray();
            List<String> attrValues = new ArrayList<>(); //属性值的value
            for (Integer valueId : valueArray) {
                attrValues.add(attributeValueMapper.selectById(valueId).getValue());
            }

            Sku sku = new Sku();
            sku.setPrice(datum.getPrice());
            sku.setProductId(requestVo.getProductId());
            sku.setStockNum(datum.getStockNum());
            sku.setGmtCreate(gmtCreate);

            String productAttribute = "";
            productAttribute += "{";

            for (int i = 0; i < attrValues.size(); i++) {
                productAttribute += ("\"" + attrNames.get(i) + "\"" + ":" + "\"" + attrValues.get(i) + "\"");
                if(i != attrValues.size() - 1) {
                    productAttribute += ",";
                }
            }

            productAttribute += "}";

            sku.setProductAttribute(productAttribute);

            skuMapper.insert(sku);

            // 获取商品的最高、低价
            highestPrice = (datum.getPrice().compareTo(highestPrice) == 1)? datum.getPrice(): highestPrice;
            lowestPrice = (datum.getPrice().compareTo(lowestPrice) == -1)? datum.getPrice(): lowestPrice;

            // 获取商品库存
            stockNum += datum.getStockNum();

        }

        /**
         * 商品表
         */
        productDB.setDescription(requestVo.getDescription());
        productDB.setImagePath(requestVo.getImagePath());
        productDB.setName(requestVo.getName());
        productDB.setTypeId(requestVo.getTypeId());
        productDB.setHighestPrice(highestPrice);
        productDB.setLowestPrice(lowestPrice);
        productDB.setStockNum(stockNum);
        productMapper.updateById(productDB);
    }

    /**
     * 商家查看商品信息
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsByStore(Integer id, Integer pageNum, Integer pageSize) {
        if(id == null || pageNum == null || pageSize == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", id);
        Page<Product> page = new Page<>(pageNum, pageSize);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));
        return responseVo;
    }

    /**
     * 商家筛选查看商品
     * @param requestVo
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> screenProducts(ScreenProductsRequestVo requestVo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", requestVo.getStoreId());

        if(requestVo.getName() != null) {
            String likeName = "";
            String[] split = requestVo.getName().split("");
            for (int i = 0; i < split.length; i++) {
                likeName += split[i];
                if(i != split.length - 1) {
                    likeName += "%";
                }
            }
            queryWrapper.like("name", likeName);
        }

        if(requestVo.getTypeId() != null) {
            queryWrapper.eq("type_id", requestVo.getTypeId());
        }

        if(requestVo.getStockNum() != null) {
            queryWrapper.gt("stock_num", requestVo.getStockNum());
        }

        Page<Product> page = new Page<>(requestVo.getPageNum(), requestVo.getPageSize());
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));
        return responseVo;
    }

    /**
     * 顾客按分类查看商品
     * @param typeId
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsByType(Integer typeId, Integer pageNum, Integer pageSize) {
        if(typeId == null || pageNum == null || pageSize == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type_id", typeId);

        Page<Product> page = new Page<>(pageNum, pageSize);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVos = new PageResponseVo(productIPage);
        responseVos.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));

        return responseVos;
    }

    /**
     * 顾客按商品名搜索商品
     * @param name
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsByProductName(String name, Integer pageNum, Integer pageSize) {
        if(name == null || pageNum == null || pageSize == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        String likeName = "";
        String[] split = name.split("");
        for (int i = 0; i < split.length; i++) {
            likeName += split[i];
            if(i != split.length - 1) {
                likeName += "%";
            }
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("name", likeName);

        Page<Product> page = new Page<>(pageNum, pageSize);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));

        return responseVo;
    }

    /**
     * 顾客按商店搜索商品
     * @param storeId
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsByStoreId(Integer storeId, Integer pageNum, Integer pageSize) {
        if(storeId == null || pageNum == null || pageSize == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("store_id", storeId);

        Page<Product> page = new Page<>(pageNum, pageSize);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        return responseVo;
    }

    /**
     * 顾客按商品id查看商品（和店铺）信息
     * @param id 商品id
     * @return
     */
    @Override
    public ProductWithStoreQueryResponseVo getProductWithStoreById(Integer id) {
        if(id == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        ProductWithStoreQueryResponseVo responseVo = new ProductWithStoreQueryResponseVo();

        Product productDB = productMapper.selectById(id);
        if(productDB == null) {
            LOG.error("商品不存在");
            throw new RuntimeException();
        }
        ProductQueryResponseVo product = CopyUtil.copy(productDB, ProductQueryResponseVo.class);

        Store storeDB = storeMapper.selectById(productDB.getStoreId());
        StoreQueryResponseVo store = CopyUtil.copy(storeDB, StoreQueryResponseVo.class);

        responseVo.setProduct(product);
        responseVo.setStore(store);

        return responseVo;
    }

    /**
     * 消费者查看商品（首页，一般用于未登录用户，按销量排序）
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsBySalesNum(Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        Page<Product> page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("sales_num");
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));

        return responseVo;
    }
}
