package com.stack.dogcat.gomall.job;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Kou Xiaoyu
 * @Date 2021/7/31
 */
@Component
public class OrderCheckJob {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    SkuMapper skuMapper;

    /**
     * 判断订单是否过期
     */
    @Scheduled(cron= "0 0/5 * * * ? ")
    @Transactional
    public void checkJobOnOrder(){
        List<Order> orders = orderMapper.selectList(new QueryWrapper<Order>().eq("status",0));
        for(Order order:orders){
            if(LocalDateTime.now().minusMinutes(30).isAfter(order.getGmtCreate())){
                order.setStatus(4);
                orderMapper.updateById(order);
                Sku sku = skuMapper.selectById(order.getSkuId());
                sku.setStockNum(sku.getStockNum()+1);
                skuMapper.updateById(sku);
                Product product = productMapper.selectById(order.getProductId());
                product.setStockNum(product.getStockNum()+1);
                productMapper.updateById(product);
            }
        }
    }

}
