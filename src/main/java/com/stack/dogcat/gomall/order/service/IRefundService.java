package com.stack.dogcat.gomall.order.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.order.entity.Refund;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.order.responseVo.RefundResponseVo;

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
    void handleRefundApply(Integer refundId,Integer option);

}
