package com.stack.dogcat.gomall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.AttributeNameMapper;
import com.stack.dogcat.gomall.product.mapper.AttributeValueMapper;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.product.requestVo.ProductSaveRequestVo;
import com.stack.dogcat.gomall.product.responseVo.ProductWithAttrbutes;
import com.stack.dogcat.gomall.product.service.IProductService;
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

        List<Sku> skus = new ArrayList<>();
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
                productAttribute += (attrNames.get(i) + ":" + attrValues.get(i));
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
}
