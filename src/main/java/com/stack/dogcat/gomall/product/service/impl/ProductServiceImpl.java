package com.stack.dogcat.gomall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.mapper.ProductCollectionMapper;
import com.stack.dogcat.gomall.message.entity.Comment;
import com.stack.dogcat.gomall.message.mapper.CommentMapper;
import com.stack.dogcat.gomall.order.mapper.CartItemMapper;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.AttributeNameMapper;
import com.stack.dogcat.gomall.product.mapper.AttributeValueMapper;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.product.requestVo.ProductSaveRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ProductUpdateRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ScreenProductsRequestVo;
import com.stack.dogcat.gomall.product.responseVo.*;
import com.stack.dogcat.gomall.product.service.IProductService;
import com.stack.dogcat.gomall.sales.entity.Coupon;
import com.stack.dogcat.gomall.sales.entity.SalesPromotion;
import com.stack.dogcat.gomall.sales.mapper.CouponMapper;
import com.stack.dogcat.gomall.sales.mapper.SalesPromotionMapper;
import com.stack.dogcat.gomall.sales.responseVo.CouponInfoResponseVo;
import com.stack.dogcat.gomall.sales.responseVo.SalesPromotionQueryResponseVo;
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
 * ?????? ???????????????
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

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    ProductCollectionMapper productCollectionMapper;

    @Autowired
    CartItemMapper cartItemMapper;

    @Autowired
    SalesPromotionMapper salesPromotionMapper;

    @Autowired
    CommentMapper commentMapper;

    /**
     * ??????????????????
     * @param requestVo
     */
    @Override
    @Transactional
    public void saveProduct(ProductSaveRequestVo requestVo) {

        LocalDateTime now = LocalDateTime.now();

        // ???????????????????????????
        BigDecimal highestPrice = BigDecimal.valueOf(0.0);
        BigDecimal lowestPrice = BigDecimal.valueOf(Double.MAX_VALUE);

        // ???????????????
        Integer stockNum = 0;

        /**
         * ?????????
         */
        Product product = new Product();
        product.setDescription(requestVo.getDescription());
        product.setGmtCreate(now);
        product.setImagePath(requestVo.getImagePath());
        product.setName(requestVo.getName());
        product.setStoreId(requestVo.getStoreId());
        product.setTypeId(requestVo.getTypeId());
        product.setStockNum(0); //???????????????????????????????????????0??????????????????????????????
        productMapper.insert(product);

        // ???????????????????????????
        Product productDB = productMapper.selectById(product.getId());

        /**
         * sku???
         */
        //??????????????????????????????
        //"{\"attrNameIdArray\":[1, 2],\"data\": [{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]},{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]}]}";
        //??????????????????????????????
        //"{\"attriNameIdArray\":[8,9,10],\"data\":[{\"stockNum\":\"20\",\"price\":\"4899\",\"valueArray\":[8,12,13]},{\"stockNum\":\"40\",\"price\":\"6899\",\"valueArray\":[8,21,13]}]}"
        String skusString = requestVo.getSkusString();
        JSONObject parse = JSON.parseObject(skusString);

        // ??????attrNameIdArray
        JSONArray attrNameIdJSONArray = parse.getJSONArray("attrNameIdArray");
        List<Integer> attrNameIdArray = attrNameIdJSONArray.toJavaList(Integer.class);
        List<String> attrNames = new ArrayList<>(); //????????????name
        for (Integer id : attrNameIdArray) {
            attrNames.add(attributeNameMapper.selectById(id).getName());
        }

        // ??????data
        JSONArray dataJSONArray = parse.getJSONArray("data");
        List<ProductWithAttrbutes> data = dataJSONArray.toJavaList(ProductWithAttrbutes.class);

        for (ProductWithAttrbutes datum : data) {
            List<Integer> valueArray = datum.getValueArray();
            List<String> attrValues = new ArrayList<>(); //????????????value
            for (Integer valueId : valueArray) {
                attrValues.add(attributeValueMapper.selectById(valueId).getValue());
            }

            Sku sku = new Sku();
            sku.setGmtCreate(now);
            sku.setPrice(datum.getPrice());
            sku.setProductId(productDB.getId());
            sku.setStockNum(datum.getStockNum());

            String productAttribute = "";
//            productAttribute += "{";

            for (int i = 0; i < attrValues.size(); i++) {
                productAttribute += ("\"" + attrNames.get(i) + "\"" + ":" + "\"" + attrValues.get(i) + "\"");
                if(i != attrValues.size() - 1) {
                    productAttribute += ",";
                }
            }

//            productAttribute += "}";

            sku.setProductAttribute(productAttribute);

            skuMapper.insert(sku);

            // ??????????????????????????????
            highestPrice = (datum.getPrice().compareTo(highestPrice) == 1)? datum.getPrice(): highestPrice;
            lowestPrice = (datum.getPrice().compareTo(lowestPrice) == -1)? datum.getPrice(): lowestPrice;

            // ??????????????????
            stockNum += datum.getStockNum();

        }

        // ?????????????????????????????????????????????????????????
        productDB.setHighestPrice(highestPrice);
        productDB.setLowestPrice(lowestPrice);
        productDB.setStockNum(stockNum);
        productMapper.updateById(productDB);

    }

    /**
     * ??????????????????
     * @param id
     */
    @Override
    public void deleteProductById(Integer id) {
        if(id == null) {
            LOG.error("??????????????????");
            throw new RuntimeException();
        }
        Product productDB = productMapper.selectById(id);
        productDB.setStatus(0);
        productMapper.updateById(productDB);

        /**
         * ??????????????????????????????????????????
         */
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", id);
        productCollectionMapper.delete(queryWrapper);
        cartItemMapper.delete(queryWrapper);
        salesPromotionMapper.delete(queryWrapper);
    }

//    /**
//     * ????????????????????????
//     * @param requestVo
//     */
//    @Override
//    public void updateProducts(ProductUpdateRequestVo requestVo) {
//
//        Product productDB = productMapper.selectById(requestVo.getProductId());
//
//        LocalDateTime gmtCreate = productDB.getGmtCreate();
//
//        // ???????????????????????????
//        BigDecimal highestPrice = BigDecimal.valueOf(0.0);
//        BigDecimal lowestPrice = BigDecimal.valueOf(Double.MAX_VALUE);
//
//        // ???????????????
//        Integer stockNum = 0;
//
//        /**
//         * sku???
//         */
//        //????????????????????????sku??????????????????????????????sku??????
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("product_id", requestVo.getProductId());
//        List<Sku> skusDB = skuMapper.selectList(queryWrapper);
//        if(skusDB != null) {
//            for (Sku sku : skusDB) {
//                sku.setStatus(1);
//                skuMapper.updateById(sku);
//            }
//        }
//
//        //"{\"attrNameIdArray\":[1, 2],\"data\": [{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]},{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]}]}";
//        String skusString = requestVo.getSkusString();
//        JSONObject parse = JSON.parseObject(skusString);
//
//        // ??????attrNameIdArray
//        JSONArray attrNameIdJSONArray = parse.getJSONArray("attrNameIdArray");
//        List<Integer> attrNameIdArray = attrNameIdJSONArray.toJavaList(Integer.class);
//        List<String> attrNames = new ArrayList<>(); //????????????name
//        for (Integer id : attrNameIdArray) {
//            attrNames.add(attributeNameMapper.selectById(id).getName());
//        }
//
//        // ??????data
//        JSONArray dataJSONArray = parse.getJSONArray("data");
//        List<ProductWithAttrbutes> data = dataJSONArray.toJavaList(ProductWithAttrbutes.class);
//
//        for (ProductWithAttrbutes datum : data) {
//            List<Integer> valueArray = datum.getValueArray();
//            List<String> attrValues = new ArrayList<>(); //????????????value
//            for (Integer valueId : valueArray) {
//                attrValues.add(attributeValueMapper.selectById(valueId).getValue());
//            }
//
//            Sku sku = new Sku();
//            sku.setPrice(datum.getPrice());
//            sku.setProductId(requestVo.getProductId());
//            sku.setStockNum(datum.getStockNum());
//            sku.setGmtCreate(gmtCreate);
//
//            String productAttribute = "";
////            productAttribute += "{";
//
//            for (int i = 0; i < attrValues.size(); i++) {
//                productAttribute += ("\"" + attrNames.get(i) + "\"" + ":" + "\"" + attrValues.get(i) + "\"");
//                if(i != attrValues.size() - 1) {
//                    productAttribute += ",";
//                }
//            }
//
////            productAttribute += "}";
//
//            sku.setProductAttribute(productAttribute);
//
//            skuMapper.insert(sku);
//
//            // ??????????????????????????????
//            highestPrice = (datum.getPrice().compareTo(highestPrice) == 1)? datum.getPrice(): highestPrice;
//            lowestPrice = (datum.getPrice().compareTo(lowestPrice) == -1)? datum.getPrice(): lowestPrice;
//
//            // ??????????????????
//            stockNum += datum.getStockNum();
//
//        }
//
//        /**
//         * ?????????
//         */
//        productDB.setDescription(requestVo.getDescription());
//        productDB.setImagePath(requestVo.getImagePath());
//        productDB.setName(requestVo.getName());
//        productDB.setTypeId(requestVo.getTypeId());
//        productDB.setHighestPrice(highestPrice);
//        productDB.setLowestPrice(lowestPrice);
//        productDB.setStockNum(stockNum);
//        productMapper.updateById(productDB);
//    }

    /**
     * ????????????????????????
     * @param requestVo
     */
    @Override
    public void updateProducts(ProductUpdateRequestVo requestVo) {

        Product productDB = productMapper.selectById(requestVo.getProductId());
        productDB.setName(requestVo.getName());
        productDB.setDescription(requestVo.getDescription());
        productDB.setTypeId(requestVo.getTypeId());
        productDB.setImagePath(requestVo.getImagePath());

        /**
         * ?????????
         */
        productDB.setDescription(requestVo.getDescription());
        productDB.setImagePath(requestVo.getImagePath());
        productDB.setName(requestVo.getName());
        productDB.setTypeId(requestVo.getTypeId());
        productMapper.updateById(productDB);
    }

    /**
     * ????????????????????????
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponseVo<ProductWithCommentResponseVo> listProductsByStore(Integer id, Integer pageNum, Integer pageSize) {
        if(id == null || pageNum == null || pageSize == null) {
            LOG.error("??????????????????");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", id);
        queryWrapper.eq("status", 1);
        Page<Product> page = new Page<>(pageNum, pageSize);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductWithCommentResponseVo> responseVos = new PageResponseVo(productIPage);
        List<Product> products = productIPage.getRecords();
        List<ProductWithCommentResponseVo> responseVoList=new ArrayList<>();
        for (Product product:products) {
            ProductWithCommentResponseVo responseVo = new ProductWithCommentResponseVo();
            responseVo.setId(product.getId());
            responseVo.setImagePath(product.getImagePath());
            responseVo.setName(product.getName());
            responseVo.setDescription(product.getDescription());
            responseVo.setGmtCreate(product.getGmtCreate());
            responseVo.setStockNum(product.getStockNum());
            responseVo.setCommentNum(commentMapper.selectCount(new QueryWrapper<Comment>().eq("product_id",product.getId())));
            responseVoList.add(responseVo);
        }
        responseVos.setData(responseVoList);
        return responseVos;
    }

    /**
     * ????????????????????????
     * @param requestVo
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> screenProducts(ScreenProductsRequestVo requestVo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", requestVo.getStoreId());
        queryWrapper.eq("status", 1);

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
            if(requestVo.getStockNum() == 0) {
                queryWrapper.eq("stock_num", 0);
            } else if(requestVo.getStockNum() == 1) {
                queryWrapper.lt("stock_num", 10);
                queryWrapper.gt("stock_num", 0);
            } else if(requestVo.getStockNum() == 2) {
                queryWrapper.lt("stock_num", 50);
                queryWrapper.gt("stock_num", 10);
            } else {
                queryWrapper.gt("stock_num", 50);
            }
        }

        if(requestVo.getColumnName() != null && requestVo.getColumnOrder() != null) {
            if(requestVo.getColumnOrder().equals("ascending")) {
                queryWrapper.orderByAsc(requestVo.getColumnName());
            } else {
                queryWrapper.orderByDesc(requestVo.getColumnName());
            }
        }

        Page<Product> page = new Page<>(requestVo.getPageNum(), requestVo.getPageSize());
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));
        return responseVo;
    }

    /**
     * ???????????????????????????
     * @param typeId
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsByType(Integer typeId, Integer pageNum, Integer pageSize) {
        if(typeId == null || pageNum == null || pageSize == null) {
            LOG.error("??????????????????");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type_id", typeId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("sales_num");

        Page<Product> page = new Page<>(pageNum, pageSize);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVos = new PageResponseVo(productIPage);
        responseVos.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));

        return responseVos;
    }

    /**
     * ??????????????????????????????
     * @param name
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsByProductName(String name, Integer pageNum, Integer pageSize) {
        if(name == null || pageNum == null || pageSize == null) {
            LOG.error("??????????????????");
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
        queryWrapper.eq("status", 1);

        Page<Product> page = new Page<>(pageNum, pageSize);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));

        return responseVo;
    }

    /**
     * ???????????????????????????
     * @param storeId
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsByStoreId(Integer storeId, Integer pageNum, Integer pageSize) {
        if(storeId == null || pageNum == null || pageSize == null) {
            LOG.error("??????????????????");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", storeId);
        queryWrapper.eq("status", 1);

        Page<Product> page = new Page<>(pageNum, pageSize);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        return responseVo;
    }

    /**
     * ???????????????id?????????????????????????????????
     * @param id ??????id
     * @return
     */
    @Override
    public ProductWithStoreQueryResponseVo getProductWithStoreById(Integer id) {
        if(id == null) {
            LOG.error("??????????????????");
            throw new RuntimeException();
        }

        ProductWithStoreQueryResponseVo responseVo = new ProductWithStoreQueryResponseVo();

        Product productDB = productMapper.selectById(id);
        if(productDB == null) {
            LOG.error("???????????????");
            throw new RuntimeException();
        }
        if(productDB.getStatus() == 0) {
            LOG.error("???????????????");
            throw new RuntimeException();
        }
        ProductQueryResponseVo product = CopyUtil.copy(productDB, ProductQueryResponseVo.class);

        Store storeDB = storeMapper.selectById(productDB.getStoreId());
        StoreQueryResponseVo store = CopyUtil.copy(storeDB, StoreQueryResponseVo.class);

        LocalDateTime now = LocalDateTime.now();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", storeDB.getId());
        queryWrapper.lt("start_time", now);
        queryWrapper.gt("deadline", now);
        List<Coupon> couponsDB = couponMapper.selectList(queryWrapper);
        List<CouponInfoResponseVo> couponInfoResponseVos = CopyUtil.copyList(couponsDB, CouponInfoResponseVo.class);

        List<SalesPromotion> salesPromotionsDB = new ArrayList<>();
        List<SalesPromotionQueryResponseVo> salesPromotionQueryResponseVos = new ArrayList<>();
        if(productDB.getIsOnsale() == 1) {
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("product_id", id);
            queryWrapper.gt("deadline", now);
            salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
            salesPromotionQueryResponseVos = CopyUtil.copyList(salesPromotionsDB, SalesPromotionQueryResponseVo.class);
        }
//        if(salesPromotionsDB == null || salesPromotionsDB.size() != 1) {
//            LOG.error("??????????????????");
//            throw new RuntimeException();
//        }

        responseVo.setProduct(product);
        responseVo.setStore(store);
        responseVo.setCoupons(couponInfoResponseVos);
        responseVo.setSales(salesPromotionQueryResponseVos);

        return responseVo;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listProductsBySalesNum(Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize == null) {
            LOG.error("??????????????????");
            throw new RuntimeException();
        }

        Page<Product> page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("sales_num");
        queryWrapper.eq("status", 1);
        IPage<Product> productIPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
        responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));

        return responseVo;
    }

    /**
     * ???id????????????
     * @param id
     * @return
     */
    @Override
    public ProductQueryResponseVo getProductById(Integer id) {
        Product productDB = productMapper.selectById(id);
        if(productDB == null) {
            LOG.info("??????????????????");
            throw new RuntimeException();
        }
        return CopyUtil.copy(productDB, ProductQueryResponseVo.class);
    }

    /**
     * ?????????id???????????????????????????????????????
     * @param ids ??????id????????????????????????
     * @return
     */
    @Override
    public List<ProductWithStoreQueryResponseVo> getProductWithStoreByIds(String ids) {
        String[] idStrList = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < idStrList.length; i++) {
            idList.add(Integer.parseInt(idStrList[i]));
        }

        List<ProductWithStoreQueryResponseVo> responseVos = new ArrayList<>();
        for (Integer id : idList) {
            ProductWithStoreQueryResponseVo vo = getProductWithStoreById(id);

            responseVos.add(vo);
        }
        return responseVos;
    }

    /**
     * ????????????
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponseVo<ProductQueryResponseVo> listNewProducts(Integer pageNum, Integer pageSize) {
        if(pageNum == null || pageSize == null) {
            LOG.error("??????????????????");
            throw new RuntimeException();
        }

        Page<Product> page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("gmt_create");
        IPage<Product> iPage = productMapper.selectPage(page, queryWrapper);
        PageResponseVo<ProductQueryResponseVo> pageResponseVo = new PageResponseVo(iPage);
        pageResponseVo.setData(CopyUtil.copyList(iPage.getRecords(), ProductQueryResponseVo.class));
        return pageResponseVo;
    }
}
