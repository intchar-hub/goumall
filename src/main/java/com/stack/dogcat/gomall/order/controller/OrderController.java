package com.stack.dogcat.gomall.order.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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

    @PostMapping("/listOrderByCustomer")
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
    public SysResult payOrder (HttpServletRequest httpRequest, HttpServletResponse httpResponse, Integer orderId) throws ServletException, IOException {


        Order order = orderMapper.selectById(orderId);
        OrderInfoResponseVo orderInfoResponseVo = orderService.getOrderInfo(orderId,1);

        //获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient( "https://openapi.alipaydev.com/gateway.do" , AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);
        /**
        //创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        //
        alipayRequest.setNotifyUrl("http://localhost:8081/order/order/payNotify");

        alipayRequest.setBizContent("{" +
                "\"out_trade_no\":"+"\""+order.getOrderNumber()+"\""+","+
                "\"timeout_express\":\"10m\","+
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "\"total_amount\":"+"\""+order.getTotalPrice().toString()+"\""+","+
                "\"subject\":"+"\""+orderInfoResponseVo.getProductName()+"\""+","+
                "\"body\":"+"\""+orderInfoResponseVo.getProductName()+"\""+
                "}");

        String form="";
        try {
            AlipayTradeWapPayResponse response =alipayClient.pageExecute(alipayRequest);
            if(response.isSuccess()){
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }

            form = response.getBody();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return SysResult.build(200,"操作成功",form); **/

        try {
            //（1）封装bizmodel信息
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            model.setOutTradeNo(order.getOrderNumber());
            model.setSubject(orderInfoResponseVo.getProductName());
            model.setBody(orderInfoResponseVo.getProductName());
            model.setProductCode("QUICK_WAP_WAY");
            model.setSellerId("2088621956175664");
            model.setTotalAmount(order.getTotalPrice().toString());
            model.setTimeoutExpress("10m");
            model.setQuitUrl("https://www.hao123.com/");
            //（2）设置请求参数
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            //alipayRequest.setReturnUrl();
            alipayRequest.setNotifyUrl("http://localhost:8081/order/order/payNotify");
            alipayRequest.setReturnUrl("https://www.hao123.com/");
            alipayRequest.setBizModel(model);
            //（3）请求
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            System.out.println("*********************\n返回结果为："+form);
            return SysResult.build(200,"操作成功",form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
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


}
