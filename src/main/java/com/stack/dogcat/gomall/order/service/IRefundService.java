package com.stack.dogcat.gomall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.entity.Refund;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.order.responseVo.OrderInfoResponseVo;
import com.stack.dogcat.gomall.order.responseVo.RefundListInfo;
import com.stack.dogcat.gomall.order.responseVo.RefundOrderInfo;
import com.stack.dogcat.gomall.order.responseVo.RefundResponseVo;

import java.text.ParseException;

/**
 * <p>
 * 退款表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IRefundService extends IService<Refund> {

    /**消费者申请订单退款*/
    void saveRefund(Integer orderId,String reason);

    /**商家查看退款申请*/
    PageResponseVo<RefundResponseVo>listRefundByStoreId(Integer storeId,Integer status,Integer pageNum,Integer pageSize);

    /**商家处理退款申请*/
    String handleRefundApply(Integer refundId,Integer option) throws InterruptedException;

    /**支付宝退款*/
    String aliPayRefund(String out_trade_no,String refund_amount);

    /**支付宝退款查询*/
    boolean aliPayRefundQuery(String out_trade_no);

    /**消费者撤销退款申请*/
    void cancelRefund(Integer refundId);

    /**消费者查看退款订单列表*/
    PageResponseVo<RefundListInfo> listRefundByCustomerId(Integer customerId,Integer pageNum,Integer pageSize);

    /**消费者查看退款订单详情*/
    OrderInfoResponseVo getRefundOrderInfo(Integer refundId);

    /**商家按条件查询退款相关订单（即refund_status不为0）**/
    PageResponseVo<RefundOrderInfo>listRefundByScreenConditions(Integer storeId, Integer pageNum, Integer pageSize, String orderNumber, Integer refundStatus, String gmtCreate) throws ParseException;

}
