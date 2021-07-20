package com.stack.dogcat.gomall.order.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
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

    //应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    String APP_ID="2021000117691523";

    // 商户私钥，您的PKCS8格式RSA2私钥
    String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCgiAFZCotl7Mqy2niWN0jgqVupFPdkq66ZTSdeZ" +
            "ZagooarSAwzeIsKY8OiIFnrqtIY2Q0HzMhlEU+jqwT0jITR7L6xW5kO4K5zsbUBZdRGVaQyBF0Y36JhoA9Fg+GpIBjo51dRFqoC57pGuBtH" +
            "2b+augYfFuRKBTYnE1qLMdKFKjKaMGQ3aMQ2Iih84M5Rsc+cOAZ96nH2AdKnOOqV8YBQUteYNRnYn3UthguEuslRbpG69zZf8KFAqjxAv64" +
            "D4ugl2urJka9VEN6fX7aU3nSuO8S1I+4QETnEzHy3FXUJDuza0WjpZgKNKQWBwQWzJs17C50ruJFlwqjLxjV0/GqRAgMBAAECggEAadbh1J" +
            "wAJlZFmkAyaw/OK9ldRpA9QBF1TzuwLiuacRRW58zbxn++ZD48eMMNpjR8yscoMTFSGlicglM6NCYFWAbX/0VeF5IJUtwic+Z4W1sRo6x+2" +
            "OxxOh52kyWNfZ1c7wm/5wWjV8ECbqfpzKiDDHeDm7HTF/5xore+5bEWlJafIEvniaA+GYmnE5h8Ze5K/F68teU5P3q7sbH/jv4lyQt94YFc" +
            "zMmVUDwntxHxM7E4IMBwRjMtXtR83SP1MLjwLVIHTizrEEVSDVDM1g9dpuYZbG8/71skqM6Pi6eLxBIlYBhP3ko3prcP3UC9I90uUeUxYAF" +
            "2zmwPXFiQNeNO8QKBgQDUtvXjF7t2xk6r828r6v4EKGkILhDewQKCIpPYK3gr81cXA4ZTQvxxwxoyLuPxYqZ/FuzfUfTBVukb2/7CTxjtnW" +
            "ai9j0qldbc+6AKIPIZpwSXJ9LYIxbFfuusecCNd/zeFqdz67F+fXKJg0/9OyDtf0xLyhEh5g/Qvb5u29sXfQKBgQDBMqGNtGNVlyrjyntYU" +
            "P17Cd1oXzsVL0YwyfQkMKaT5gIOfA1lrGcw7iXqCSJgMmdJwiPwxFfdLQSs/p/0KybWQS3Qg0rLcfjl5wM7ZIwodx3doXTqfn6FeoMHhwvP" +
            "JDX1a0QIItWtPQbNTXGX9heyAFwQ3EK0fhZ/od6hpKgTpQKBgQC/UnjC8xuX7zHfkysQ82HZ14bw5o4h7LQW3IbhzedekJZqiq8MwJlSsUs" +
            "ki+xVtodOlA4lpw9fvo4Ykr0HhCFFx2cDeDr3zKHfTStbMNrQm9qTIiyQXq8in8/V6AR6uctk7jdbGkkhHjAg40zU1ZP+SZsUKAFU3hqng6" +
            "J/D5x1+QKBgQCrO5rgo68fHzqOZtpn7nCb6qRoa29MWXgWqghPQyeBEm1kNSMSqHlVbt3/zlS/eiz9vKpqLTvK9qmcQpm2qcBkSaS3mIZkR" +
            "wHZkqTUsUtgWiMIWp/rbm3pFCqqM1GOKIQCny3PIEDOqLYspKU3kJBz3cm7z9z3aIG8YdWW4v9tWQKBgFPga2XaR/7QuIhXoJuEA7srRP52" +
            "W0lF2UW2fUW9zuiRawqqH1jRODDWgpwwkpLuuiTA64IltoBRyrHJ7meA3PqdNIsorjTqkcQaT68M7QxZvfnnc0puax9UEbf0KCNbXjxxQC/" +
            "r3fJ1zxyjy9CVQSGrk1lWbWezNsRd2ASnBlg9";

    String FORMAT="json";
    String CHARSET="utf-8";
    String SIGN_TYPE="RSA2";

    //公钥
    String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoIgBWQqLZezKstp4ljdI4KlbqRT3ZKuumU0nXmWWoKKGq0" +
            "gMM3iLCmPDoiBZ66rSGNkNB8zIZRFPo6sE9IyE0ey+sVuZDuCuc7G1AWXURlWkMgRdGN+iYaAPRYPhqSAY6OdXURaqAue6RrgbR9m/mroGH" +
            "xbkSgU2JxNaizHShSoymjBkN2jENiIofODOUbHPnDgGfepx9gHSpzjqlfGAUFLXmDUZ2J91LYYLhLrJUW6Ruvc2X/ChQKo8QL+uA+LoJdrq" +
            "yZGvVRDen1+2lN50rjvEtSPuEBE5xMx8txV1CQ7s2tFo6WYCjSkFgcEFsybNewudK7iRZcKoy8Y1dPxqkQIDAQAB";



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
        AlipayClient alipayClient = new DefaultAlipayClient( "\t\n" + "https://openapi.alipaydev.com/gateway.do" , APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        //创建API对应的request
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();

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
            AlipayTradeAppPayResponse response =alipayClient.sdkExecute(alipayRequest);
            if(response.isSuccess()){
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }


            form = response.getBody();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }



        return SysResult.success(form);
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
