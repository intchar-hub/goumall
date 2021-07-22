package com.stack.dogcat.gomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.product.requestVo.SkuUpdateRequestVo;
import com.stack.dogcat.gomall.product.responseVo.SkuQueryResponseVo;
import com.stack.dogcat.gomall.product.service.ISkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品规格和库存量单元 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    ProductMapper productMapper;

    /**
     * 消费者根据商品id查看商品规格
     * @param productId
     * @return
     */
    @Override
    public List<SkuQueryResponseVo> listSkuByProductId(Integer productId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", productId);
        List<Sku> skusDB = skuMapper.selectList(queryWrapper);
        List<SkuQueryResponseVo> responseVos = CopyUtil.copyList(skusDB, SkuQueryResponseVo.class);
        return responseVos;
    }

    /**
     * 修改某一商品的sku
     * @param requestVos
     */
    @Override
    @Transactional
    public void updateSku(List<SkuUpdateRequestVo> requestVos) {
        Integer stockNum = 0;
        Integer productId = 0;
        BigDecimal lowestPrice = new BigDecimal(Float.MAX_VALUE);
        BigDecimal highestPrice = new BigDecimal(0.0);
        for (SkuUpdateRequestVo requestVo : requestVos) {
            Sku skuDB = skuMapper.selectById(requestVo.getId());
            if(skuDB == null) {
                throw new RuntimeException("sku更改：无此sku");
            }
            skuDB.setStockNum(requestVo.getStockNum());
            skuDB.setPrice(requestVo.getPrice());
            skuMapper.updateById(skuDB);

            productId = skuDB.getProductId();
            stockNum += requestVo.getStockNum();
            lowestPrice = (requestVo.getPrice().compareTo(lowestPrice) == -1)? requestVo.getPrice(): lowestPrice;
            highestPrice = (requestVo.getPrice().compareTo(highestPrice) == 1)? requestVo.getPrice(): highestPrice;
        }

        Product productDB = productMapper.selectById(productId);
        productDB.setStockNum(stockNum);
        productDB.setLowestPrice(lowestPrice);
        productDB.setHighestPrice(highestPrice);

        productMapper.updateById(productDB);
    }
}
