package com.stack.dogcat.gomall.order.controller;


import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.responseVo.RefundResponseVo;
import com.stack.dogcat.gomall.order.service.impl.RefundServiceImpl;
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
    @PostMapping("/listRefundByStoreId")
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

        try{
            refundService.handleRefundApply(refundId,option);

        }catch (Exception e){
            return SysResult.error(e.getMessage());
        }
        return  SysResult.success();
    }

}
