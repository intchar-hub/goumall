package com.stack.dogcat.gomall.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.config.AlipayConfig;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;

import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.stack.dogcat.gomall.annotation.CurrentUser;
import com.stack.dogcat.gomall.annotation.Token;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.RequestVo.OrderRequestVo;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.order.service.impl.OrderServiceImpl;
import com.stack.dogcat.gomall.order.responseVo.OrderInfoResponseVo;
import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.utils.AppConst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

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
            logger.info("save_order->current_customer.id:{},current_customer.name:{},save_order_pro.id:{},save_order_store.id:{},save_order_pro.num:{}",
                    new Object[]{current_customer.getId().toString(), current_customer.getUserName()
                            , orderRequestVo.getProductId().toString(), orderRequestVo.getStoreId().toString(),
                            orderRequestVo.getProductNum().toString()});

            Integer customerId =current_customer.getId();
            SysResult result=orderService.saveOrder(customerId,orderRequestVo);
            return result;

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
    }

    /**
     * 下单(多个)
     * @param current_customer
     * @param ordersString
     * @return
     */
    @PostMapping("/payForOrders")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult payForOrders(@CurrentUser Customer current_customer,String ordersString,HttpServletResponse servletResponse){

        List<SysResult>results;
        try{

            Integer customerId =current_customer.getId();
            results = orderService.payForOrders(customerId,ordersString,servletResponse);

        }catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success(results);
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
            orderInfoResponseVo =orderService.getOrderInfo(orderId,1);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success(orderInfoResponseVo);
    }


    /**
     * 查询我的订单
     * @param current_customer
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */

    @GetMapping("/listOrderByCustomer")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult listOrderByCustomer(@CurrentUser Customer current_customer,Integer status,Integer pageNum,Integer pageSize){

        PageResponseVo<OrderInfoResponseVo>orderInfoResponseVoPageResponseVo;
        try{
            orderInfoResponseVoPageResponseVo = orderService.listOrderByCustomer(current_customer.getId(),status,pageNum,pageSize);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success(orderInfoResponseVoPageResponseVo);
    }


    /**
     * 删除订单
     * @param current_customer
     * @param orderId
     * @return
     */

    @PostMapping("/deleteOrder")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult deleteOrder(@CurrentUser Customer current_customer,Integer orderId){

        try{
            orderService.deleteOrder(current_customer.getId(),orderId);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success();
    }


    @Autowired
    OrderMapper orderMapper;

    /**
     * 支付订单
     * @param orderId
     * @return
     */

    @PostMapping("/payOrder")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult payOrder (Integer orderId,HttpServletResponse servletResponse,Integer option){

        String msg="";
        try{
            switch (option){
                case 0:
                    msg=orderService.payOrder(orderId,servletResponse);
                    break;
                case 1:
                    msg =orderService.payOrderByApp(orderId);
                    break;
                case 2:
                    msg =orderService.payOrderByWap(orderId);

            }

        } catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success(msg);

    }

    /**
     * 查询支付状态
     * @param orderId
     * @return
     */
    @GetMapping("/getPayStatus")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult getPayStatus(Integer orderId){

        Integer status;
        try{
           status = orderService.getPayStatus(orderId);

        } catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success(status);
    }

    /**
     * 支付宝支付成功后.异步请求该接口
     * @param request
     * @return
     */
    @PostMapping("/payNotify")
    @ResponseBody
    public String aliNotify(HttpServletRequest request, HttpServletResponse response) throws IOException{

        System.out.println("=支付宝异步返回支付结果开始");
        //1.从支付宝回调的request域中取值
        //获取支付宝返回的参数集合
        Map<String, String[]> aliParams = request.getParameterMap();
        //用以存放转化后的参数集合
        Map<String, String> conversionParams = new HashMap<String, String>();
        for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            String[] values = aliParams.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            conversionParams.put(key, valueStr);
        }
        System.out.println("==返回参数集合："+conversionParams);

        String status=orderService.aliNotify(conversionParams);
        return status;

    }

    /**
     * 支付宝支付成功后.异步请求该接口
     * @param request
     * @return
     */
    @PostMapping("/payOrdersNotify")
    @ResponseBody
    public String payOrdersNotify(HttpServletRequest request, HttpServletResponse response) throws IOException{

        System.out.println("=支付宝异步返回支付结果开始");
        //1.从支付宝回调的request域中取值
        //获取支付宝返回的参数集合
        Map<String, String[]> aliParams = request.getParameterMap();
        //用以存放转化后的参数集合
        Map<String, String> conversionParams = new HashMap<String, String>();
        for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            String[] values = aliParams.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            conversionParams.put(key, valueStr);
        }
        System.out.println("==返回参数集合："+conversionParams);

        String status=orderService.payOrdersNotify(conversionParams);
        return status;

    }


    /**
     * 商家按条件查询订单（与退款相关除外，即refund_status需为0），所有筛选条件下，均有：refund_status=0
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @param orderNumber
     * @param status
     * @param gmtCreate
     * @return
     */
    @GetMapping("/listOrdersByScreenConditions")
    @ResponseBody
    public SysResult listOrdersByScreenConditions(Integer storeId,Integer pageNum,Integer pageSize,String orderNumber,Integer status,String gmtCreate){

        PageResponseVo<OrderInfoResponseVo> orderPage;
        try{
            orderPage=orderService.listOrdersByScreenConditions(storeId,pageNum,pageSize,orderNumber,status,gmtCreate);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            e.printStackTrace();
            return result;
        }
        return SysResult.success(orderPage);

    }


    /**
     * 订单发货
     * @param orderId
     * @return
     */
    @PostMapping("/shiftOrder")
    @ResponseBody
    public SysResult shiftOrder(Integer orderId){

        String msg;
        try{
            msg=orderService.shiftOrder(orderId);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            e.printStackTrace();
            return result;
        }
        return SysResult.success(msg);
    }


    /**
     * 订单收货
     * @param orderId
     * @return
     */
    @PostMapping("/confirmReceipt")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult confirmReceipt(Integer orderId){

        String msg;
        try{
            msg=orderService.confirmReceipt(orderId);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success(msg);
    }


    /**
     * 订单收货
     * @param orderId
     * @return
     */
    @PostMapping("/cancelOrder")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult cancelOrder(Integer orderId){

        try{
            orderService.cancelOrder(orderId);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success();
    }
}




