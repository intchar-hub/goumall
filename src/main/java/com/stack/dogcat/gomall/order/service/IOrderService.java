package com.stack.dogcat.gomall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.RequestVo.OrderRequestVo;
import com.stack.dogcat.gomall.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.order.responseVo.OrderInfoResponseVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
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

    /**新建多个订单并支付**/
    List<SysResult> payForOrders(Integer customerId, String ordersString, HttpServletResponse servletResponse);

    /**查询单个订单**/
    OrderInfoResponseVo getOrderInfo(Integer orderId,Integer customer_visible);

    /**消费者分页查询各状态订单**/
    PageResponseVo<OrderInfoResponseVo>listOrderByCustomer(Integer customerId,Integer status,Integer pageNum,Integer pageSize);

    /**消费者删除订单**/
    void deleteOrder(Integer customerId,Integer orderId);

    /**支付订单二维码**/
    String payOrder(Integer orderId,HttpServletResponse servletResponse);

    /**支付订单App**/
    String payOrderByApp (Integer orderId);

    /**支付订单Wap**/
    String payOrderByWap (Integer orderId);

    /**Precreate*/
    String payOrderPrecreate (Integer orderId);

    /**Merge*/
    String payOrderMerge (String pre_order_no);

    /**支付状态*/
    Integer getPayStatus(Integer orderId);



    /**
     * 支付宝异步回调
     * @param conversionParams conversionParams
     * @return String
     */
    String aliNotify(Map<String, String> conversionParams);

    /**
     * 支付宝异步回调
     * @param conversionParams conversionParams
     * @return String
     */
    String payOrdersNotify(Map<String, String> conversionParams);

    /**商家按条件查询订单（与退款相关除外，即refund_status需为0），所有筛选条件下，均有：refund_status=0**/
    PageResponseVo<OrderInfoResponseVo> listOrdersByScreenConditions(Integer storeId, Integer pageNum, Integer pageSize, String orderNumber, Integer status, String gmtCreate) throws ParseException;

    /**订单发货*/
    String shiftOrder(Integer orderId);

    /**订单收货*/
    String confirmReceipt(Integer orderId);
}
