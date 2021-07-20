package com.stack.dogcat.gomall.order.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.RequestVo.OrderRequestVo;
import com.stack.dogcat.gomall.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.order.responseVo.OrderInfoResponseVo;

import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IOrderService extends IService<Order> {

    /**新建订单**/
    SysResult saveOrder(Integer customerId, OrderRequestVo orderRequestVo);

    /**查询单个订单**/
    OrderInfoResponseVo getOrderInfo(Integer orderId,Integer customer_visible);

    /**消费者分页查询各状态订单**/
    PageResponseVo<OrderInfoResponseVo>listOrderByCustomer(Integer customerId,Integer status,Integer pageNum,Integer pageSize);

    /**消费者删除订单**/
    void deleteOrder(Integer customerId,Integer orderId);


    /**
     * 支付宝异步回调
     * @param conversionParams conversionParams
     * @return String
     */
    String aliNotify(Map<String, String> conversionParams);

}
