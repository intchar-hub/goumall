package com.stack.dogcat.gomall.order.controller;


import com.stack.dogcat.gomall.annotation.CurrentUser;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.RequestVo.OrderRequestVo;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.service.impl.OrderServiceImpl;
import com.stack.dogcat.gomall.product.responseVo.OrderInfoResponseVo;
import com.stack.dogcat.gomall.user.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/order/order")
public class OrderController {

    @Autowired
    OrderServiceImpl orderService;

    /**
     * 下单
     * @param current_customer
     * @param orderRequestVo
     * @return
     */
    @PostMapping("/saveOrder")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult saveOrder(@CurrentUser Customer current_customer, OrderRequestVo orderRequestVo){

        try{
            Integer customerId =current_customer.getId();
            SysResult result=orderService.saveOrder(customerId,orderRequestVo);
            return result;

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
    }


    /**
     * 查询单个订单
     * @param orderId
     * @return
     */
    @GetMapping("/getOrderInfo")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult getOrderInfo(Integer orderId){

        OrderInfoResponseVo orderInfoResponseVo ;
        try{
            orderInfoResponseVo =orderService.getOrderInfo(orderId);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success(orderInfoResponseVo);
    }


}
