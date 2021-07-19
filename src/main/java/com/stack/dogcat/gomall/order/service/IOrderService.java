package com.stack.dogcat.gomall.order.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.RequestVo.OrderRequestVo;
import com.stack.dogcat.gomall.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.order.responseVo.OrderInfoResponseVo;

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
    OrderInfoResponseVo getOrderInfo(Integer orderId);

    /**分页查询各状态订单**/
    PageResponseVo<OrderInfoResponseVo>listOrderByCustomer(Integer customerId,Integer status,Integer pageNum,Integer pageSize);

}
