package com.stack.dogcat.gomall.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.stack.dogcat.gomall.annotation.CurrentUser;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.responseVo.OrderInfoResponseVo;
import com.stack.dogcat.gomall.order.responseVo.RefundListInfo;
import com.stack.dogcat.gomall.order.responseVo.RefundOrderInfo;
import com.stack.dogcat.gomall.order.responseVo.RefundResponseVo;
import com.stack.dogcat.gomall.order.service.impl.RefundServiceImpl;
import com.stack.dogcat.gomall.user.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 退款表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/order/refund")
public class RefundController {

    @Autowired
    RefundServiceImpl refundService;


    /**
     * 消费者退款申请
     * @param orderId
     * @param reason
     * @return
     */
    @PostMapping("/saveRefund")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult saveRefund(Integer orderId,String reason){

        try{
            refundService.saveRefund(orderId,reason);

        }catch (Exception e){
            return SysResult.error(e.getMessage());
        }
        return  SysResult.success();

    }


    /**
     * 商家查看退款申请
     * @param storeId
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/listRefundByStoreId")
    @ResponseBody
    public SysResult listRefundByStoreId(Integer storeId,Integer status,Integer pageNum,Integer pageSize){

        PageResponseVo<RefundResponseVo>responseVos;
        try{
            responseVos=refundService.listRefundByStoreId(storeId,status,pageNum,pageSize);

        }catch (Exception e){
            return SysResult.error(e.getMessage());
        }
        return  SysResult.success(responseVos);
    }


    /**
     * 商家处理退款申请
     * @param refundId
     * @param option
     * @return
     */
    @PostMapping("/handleRefundApply")
    @ResponseBody
    public SysResult handleRefundApply(Integer refundId,Integer option){

        String msg;
        try{
            msg=refundService.handleRefundApply(refundId,option);

        }catch (Exception e){
            return SysResult.error(e.getMessage());
        }
        return  SysResult.success(msg);
    }


    /**
     * 消费者撤销退款申请
     * @param orderId
     * @return
     */
    @PostMapping("/cancelRefund")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult cancelRefund(Integer orderId){

        try{
           refundService.cancelRefund(orderId);

        }catch (Exception e){
            return SysResult.error(e.getMessage());
        }
        return  SysResult.success();
    }


    /**
     * 消费者查看退款订单列表
     * @param current_customer
     * @return
     */
    @GetMapping("/listRefundByCustomerId")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult listRefundByCustomerId(@CurrentUser Customer current_customer,Integer pageNum,Integer pageSize){

        PageResponseVo<RefundListInfo> responseVo;
        try{
            responseVo=refundService.listRefundByCustomerId(current_customer.getId(),pageNum,pageSize);

        }catch (Exception e){
            return SysResult.error(e.getMessage());
        }
        return  SysResult.success(responseVo);
    }



    /**
     * 消费者查看退款订单详情
     * @param refundId
     * @return
     */
    @GetMapping("/getRefundOrderInfo")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult getRefundOrderInfo(Integer refundId){

        OrderInfoResponseVo refundOrderInfo;
        try{
            refundOrderInfo = refundService.getRefundOrderInfo(refundId);

        }catch (Exception e){
            return SysResult.error(e.getMessage());
        }
        return  SysResult.success(refundOrderInfo);
    }


    /**
     *  商家按条件查询退款相关订单（即refund_status不为0）
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param orderNumber
     * @param refundStatus
     * @param gmtCreate
     * @return
     */
    @GetMapping("/listRefundByScreenConditions")
    @ResponseBody
    public SysResult listRefundByScreenConditions(Integer storeId,Integer pageNum,Integer pageSize,String orderNumber,Integer refundStatus,String gmtCreate){

        PageResponseVo<RefundOrderInfo> orderPage;
        try{
            orderPage=refundService.listRefundByScreenConditions(storeId,pageNum,pageSize,orderNumber,refundStatus,gmtCreate);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success(orderPage);

    }
}
