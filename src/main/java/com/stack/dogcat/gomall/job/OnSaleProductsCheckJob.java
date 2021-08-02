package com.stack.dogcat.gomall.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.sales.entity.SalesPromotion;
import com.stack.dogcat.gomall.sales.mapper.SalesPromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/22 21:52
 * @Descrition TODO
 */
@Component
public class OnSaleProductsCheckJob {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    SalesPromotionMapper salesPromotionMapper;

    /**
     * 判断商品是否处于秒杀活动中、秒杀活动是否过期
     */
    @Scheduled(cron= "0 0/5 * * * ? ")
    @Transactional
    public void checkJobOnSaleProducts(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.lt("deadline", LocalDateTime.now());
        List<SalesPromotion> salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        for (SalesPromotion salesPromotion : salesPromotionsDB) {
            alterTable(salesPromotion);
        }
    }

    public void alterTable(SalesPromotion salesPromotion) {
        /**
         * 更新秒杀活动表中的status字段
         */
        salesPromotion.setStatus(1);
        salesPromotionMapper.updateById(salesPromotion);

        /**
         * 更新商品表中的is_onsale字段
         */
        Product productDB = productMapper.selectById(salesPromotion.getProductId());
        if(productDB.getStatus() == 1) {
            productDB.setIsOnsale(0);
            productMapper.updateById(productDB);
        }
    }

}
