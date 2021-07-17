package com.stack.dogcat.gomall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.RequestVo.OrderRequestVo;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.entity.ReceiveAddress;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.order.mapper.ReceiveAddressMapper;
import com.stack.dogcat.gomall.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.product.responseVo.OrderInfoResponseVo;
import com.stack.dogcat.gomall.sales.entity.Coupon;
import com.stack.dogcat.gomall.sales.mapper.CouponMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    ReceiveAddressMapper receiveAddressMapper;

    @Override
    @Transactional
    public SysResult saveOrder(Integer customerId, OrderRequestVo orderRequestVo){

        /**判断库存**/
        Map<String,Object> map =new HashMap<>();
        map.put("product_id",orderRequestVo.getProductId());
        map.put("product_attribute",orderRequestVo.getProductAttribute());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.allEq(map);
        Sku sku = skuMapper.selectOne(queryWrapper);
        if(sku.getStockNum()>=orderRequestVo.getProductNum()){
            Order order =new Order();
            order.setCustomerId(customerId);
            order.setStoreId(orderRequestVo.getStoreId());
            order.setProductId(orderRequestVo.getProductId());
            order.setSkuId(sku.getId());
            order.setCouponId(orderRequestVo.getCouponId());

            long randomSeed = System.currentTimeMillis();
            randomSeed=randomSeed+customerId+orderRequestVo.getProductId()+orderRequestVo.getStoreId()+sku.getId();
            Random r = new Random(randomSeed);
            String randNum = "";
            for (int i=0;i<8;i++)
            {
                randNum+=r.nextInt(10);
            }
            order.setOrderNumber(randNum);

            order.setProductNum(orderRequestVo.getProductNum());
            order.setPrice(sku.getPrice());
            order.setReceiveAddressId(orderRequestVo.getReceiveAddressId());

            if(orderRequestVo.getCouponId()!=null){
                order.setCouponDiscount(couponMapper.selectById(orderRequestVo.getCouponId()).getDiscount());
                order.setTotalPrice(order.getPrice().multiply(BigDecimal.valueOf(order.getProductNum())).subtract(order.getCouponDiscount()));
            }else{
                order.setTotalPrice(order.getPrice().multiply(BigDecimal.valueOf(order.getProductNum())));
            }

            order.setGmtCreate(LocalDateTime.now());

            orderMapper.insert(order);

            /**减库存**/
            sku.setStockNum(sku.getStockNum()-orderRequestVo.getProductNum());
            skuMapper.updateById(sku);

            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("order_number",randNum);
            Integer id= orderMapper.selectOne(queryWrapper1).getId();

            SysResult result=SysResult.build(200,"下单成功",id);
            return result;


        }else {
            return SysResult.error("库存不够，下单失败");
        }
    }

    @Override
    @Transactional
    public OrderInfoResponseVo getOrderInfo(Integer orderId){

        OrderInfoResponseVo  orderInfoResponseVo  = new OrderInfoResponseVo ();

        Order order = orderMapper.selectById(orderId);
        orderInfoResponseVo.setOrderNumber(order.getOrderNumber());
        orderInfoResponseVo.setProductNum(order.getProductNum());
        orderInfoResponseVo.setOrderGmtCreate(order.getGmtCreate());

        orderInfoResponseVo.setStoreName(storeMapper.selectById(order.getStoreId()).getStoreName());

        Product product = productMapper.selectById(order.getProductId());
        orderInfoResponseVo.setProductName(product.getName());
        orderInfoResponseVo.setProductImagePath(product.getImagePath());

        Sku sku = skuMapper.selectById(order.getSkuId());
        orderInfoResponseVo.setProductAttribute(sku.getProductAttribute());
        orderInfoResponseVo.setPrice(sku.getPrice());

        ReceiveAddress receiveAddress = receiveAddressMapper.selectById(order.getReceiveAddressId());
        orderInfoResponseVo.setReceiveAddress(receiveAddress.getAddress());
        orderInfoResponseVo.setPhoneNumber(receiveAddress.getPhoneNumber());

        if(order.getCouponId()!=null){
            orderInfoResponseVo.setDiscount(couponMapper.selectById(order.getCouponId()).getDiscount());
        }
        return orderInfoResponseVo;
    }
}
