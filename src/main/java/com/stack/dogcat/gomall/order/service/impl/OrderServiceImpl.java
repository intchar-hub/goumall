package com.stack.dogcat.gomall.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.stack.dogcat.gomall.QrCode.QrCodeService;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.config.AlipayConfig;
import com.stack.dogcat.gomall.message.entity.Comment;
import com.stack.dogcat.gomall.message.mapper.CommentMapper;
import com.stack.dogcat.gomall.order.RequestVo.OrderRequestVo;
import com.stack.dogcat.gomall.order.controller.OrderController;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.entity.ReceiveAddress;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.order.mapper.ReceiveAddressMapper;
import com.stack.dogcat.gomall.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.order.responseVo.OrderInfoResponseVo;
import com.stack.dogcat.gomall.sales.mapper.CouponMapper;
import com.stack.dogcat.gomall.sales.mapper.SalesPromotionMapper;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.utils.AppConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * ????????? ???????????????
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private static  Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    QrCodeService qrCodeService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    ReceiveAddressMapper receiveAddressMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SalesPromotionMapper salesPromotionMapper;

    @Override
    @Transactional
    public SysResult saveOrder(Integer customerId, OrderRequestVo orderRequestVo){


        /**????????????**/
        Map<String,Object> map =new HashMap<>();
        map.put("product_id",orderRequestVo.getProductId());
        map.put("product_attribute",orderRequestVo.getProductAttribute());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.allEq(map);
        Sku sku = skuMapper.selectOne(queryWrapper);
        if(sku.getStockNum()>=orderRequestVo.getProductNum()){
            Order order =new Order();
            order.setCustomerId(customerId);
            order.setStoreId(orderRequestVo.getStoreId());
            order.setProductId(orderRequestVo.getProductId());
            order.setSkuId(sku.getId());
            order.setCouponId(orderRequestVo.getCouponId());

            long randomSeed = System.currentTimeMillis();
            randomSeed=randomSeed+customerId+orderRequestVo.getProductId()+orderRequestVo.getStoreId()+sku.getId();
            Random r = new Random(randomSeed);
            String randNum = "";
            for (int i=0;i<8;i++)
            {
                randNum+=r.nextInt(10);
            }
            order.setOrderNumber(randNum);

            order.setProductNum(orderRequestVo.getProductNum());
            Product product =productMapper.selectById(sku.getProductId());
            if(product.getIsOnsale()==1){
                queryWrapper.clear();
                queryWrapper.eq("product_id",product.getId());
                queryWrapper.eq("status",0);
                BigDecimal discount=salesPromotionMapper.selectOne(queryWrapper).getDiscount().multiply(new BigDecimal(0.1));

                order.setPrice(sku.getPrice().multiply(discount));
            }else {
                order.setPrice(sku.getPrice());
            }
            System.out.println("price:"+order.getTotalPrice());
            ReceiveAddress receiveAddress = receiveAddressMapper.selectById(orderRequestVo.getReceiveAddressId());
            order.setReceiveAddressId(orderRequestVo.getReceiveAddressId());
            order.setConsignee(receiveAddress.getConsignee());
            order.setAddress(receiveAddress.getAddress());
            order.setPhoneNumber(receiveAddress.getPhoneNumber());

            if(orderRequestVo.getCouponId()!=null){
                order.setCouponDiscount(couponMapper.selectById(orderRequestVo.getCouponId()).getDiscount());
                order.setTotalPrice(order.getPrice().multiply(BigDecimal.valueOf(order.getProductNum())).subtract(order.getCouponDiscount()));
            }else{
                order.setTotalPrice(order.getPrice().multiply(BigDecimal.valueOf(order.getProductNum())));
            }

            order.setGmtCreate(LocalDateTime.now());

            orderMapper.insert(order);

            /**?????????**/
            sku.setStockNum(sku.getStockNum()-orderRequestVo.getProductNum());
            skuMapper.updateById(sku);
            product.setStockNum(product.getStockNum()-orderRequestVo.getProductNum());
            productMapper.updateById(product);

            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("order_number",randNum);
            Integer id= orderMapper.selectOne(queryWrapper1).getId();

            SysResult result=SysResult.build(200,"????????????",id);
            return result;


        }else {
            return SysResult.error("???????????????????????????");
        }
    }

    @Override
    @Transactional
    public List<SysResult> payForOrders(Integer customerId, String ordersString,HttpServletResponse servletResponse){

        List<SysResult>results=new ArrayList<>();
        JSONObject orderJs = JSONObject.parseObject(ordersString);
        String infoString = orderJs.getString("orders");
        JSONArray orders=JSONArray.parseArray(infoString);
        for(int i=0;i<orders.size();i++){
            JSONObject object = (JSONObject) orders.get(i);
            System.out.println(object);

            OrderRequestVo orderRequestVo=new OrderRequestVo();
            orderRequestVo.setStoreId((Integer) object.get("storeId"));
            orderRequestVo.setProductId((Integer) object.get("productId"));
            orderRequestVo.setProductAttribute((String) object.get("productAttribute"));
            orderRequestVo.setProductNum((Integer) object.get("productNum"));
            orderRequestVo.setReceiveAddressId((Integer) object.get("receiveAddressId"));
            orderRequestVo.setCouponId((Integer) object.get("couponId"));
            SysResult result = orderService.saveOrder(customerId,orderRequestVo);
            if(result.getData()==null){
                throw new RuntimeException("????????????");
            }
            results.add(result);
        }
        String out_trade_no="";
        String subject="";
        BigDecimal total_amount=new BigDecimal(0);
        for(int j=0;j<results.size();j++){
            SysResult result=results.get(j);
            OrderInfoResponseVo orderInfo=orderService.getOrderInfo((Integer) result.getData());
            out_trade_no+=orderInfo.getOrderNumber();
            subject+=orderInfo.getProductName();
            if(j!=results.size()-1){
                subject=subject+"+";
            }
            total_amount=total_amount.add(orderInfo.getTotalPrice());
        }

        //?????????????????? AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);

        try {
            //???1?????????bizmodel??????
            AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            //SDK????????????????????????????????????????????????????????????????????????????????????sdk???model????????????(model???biz_content???????????????????????????biz_content)???
            model.setOutTradeNo(out_trade_no);
            model.setSubject(subject);
            // model.setProductCode("QUICK_WAP_WAY");
            model.setSellerId("2088621956175664");
            model.setTotalAmount(total_amount.toString());
            model.setTimeoutExpress("30m");
            //model.setQuitUrl("https://www.hao123.com/");
            //???2?????????????????????
            //alipayRequest.setReturnUrl();
            alipayRequest.setNotifyUrl("http://121.36.58.203:8081/order/order/payOrdersNotify");
            //alipayRequest.setReturnUrl("https://www.hao123.com/");
            alipayRequest.setBizModel(model);
            //???3?????????
            String form = alipayClient.execute(alipayRequest).getBody();
            //System.out.println("*********************\n??????????????????" + form);

            JSONObject info = JSONObject.parseObject(form);
            String response = info.getString("alipay_trade_precreate_response");
            JSONObject response_info = JSONObject.parseObject(response);
            String qr_code = response_info.getString("qr_code");

            qrCodeService.createCodeToStream(qr_code,servletResponse);

            return results;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            results.clear();
            results.add(SysResult.error(e.getErrMsg()));
            return results;
        }

    }

    @Override
    @Transactional
    public String payOrdersNotify(Map<String, String> conversionParams){

        //????????????(???????????????????????????????????????????????????????????????)
        boolean signVerified = false;
        try {
            //??????SDK????????????
            String alipayPublicKey = AppConst.ALIPAY_PUBLIC_KEY;
            String charset = AppConst.CHARSET;
            String signType = AppConst.SIGN_TYPE;

            signVerified = AlipaySignature.rsaCheckV1(conversionParams, alipayPublicKey, charset, signType);
            //?????????????????????.
            if (signVerified) {

                // ????????????????????????????????????????????????????????????????????????????????????1\2\3\4?????????????????????????????????response?????????success?????????????????????failure ????????????????????????????????????out_trade_no???total_amount???sellerId???app_id???
                //this.check(conversionParams);
                //???????????? ??????????????????
                String tradeStatus = conversionParams.get("trade_status");

                //??????????????????????????????: ?????????????????????,????????????
                //???????????????????????????TRADE_SUCCESS???TRADE_FINISHED???????????????????????????????????????????????????
                if (tradeStatus.equals("TRADE_SUCCESS") ||tradeStatus.equals("TRADE_FINISHED")) {
                    //?????????????????????????????????????????????
                    //???????????? ?????????????????????
                    String orderNumbers = conversionParams.get("out_trade_no");
                    if(orderNumbers.length()%8!=0){
                        throw new RuntimeException("??????????????????");
                    }

                    /**??????????????????*/
                    QueryWrapper queryWrapper=new QueryWrapper();
                    int i=orderNumbers.length()/8;
                    int temp =0;
                    for(int j=0;j<i;j++){
                        String orderNumber=orderNumbers.substring(temp,temp+8);
                        temp +=8;

                        queryWrapper.eq("order_number",orderNumber);
                        Order order = orderMapper.selectOne(queryWrapper);
                        order.setStatus(1);
                        orderMapper.updateById(order);

                        Product product=productMapper.selectById(order.getProductId());
                        product.setSalesNum(product.getSalesNum()+order.getProductNum());
                        productMapper.updateById(product);

                        logger.info("order_payed->order_number:{},total_amount:{},",new Object[]{orderNumber, order.getTotalPrice()});

                    }


                    return "success";
                } else {
                    return "fail";
                }
            }else{  //???????????????

                return "fail";
            }
        } catch (AlipayApiException e) {

            e.printStackTrace();
        }
        return "fail";

    }

    @Override
    @Transactional
    public OrderInfoResponseVo getOrderInfo(Integer orderId){

        OrderInfoResponseVo  orderInfoResponseVo  = new OrderInfoResponseVo ();

        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.eq("id",orderId);

        Order order = orderMapper.selectOne(queryWrapper);

        orderInfoResponseVo.setTotalPrice(order.getTotalPrice());
        orderInfoResponseVo.setOrderId(order.getId());
        orderInfoResponseVo.setOrderNumber(order.getOrderNumber());
        orderInfoResponseVo.setProductNum(order.getProductNum());
        orderInfoResponseVo.setOrderGmtCreate(order.getGmtCreate());
        orderInfoResponseVo.setStatus(order.getStatus());
        orderInfoResponseVo.setConsignee(order.getConsignee());
        orderInfoResponseVo.setReceiveAddress(order.getAddress());
        orderInfoResponseVo.setPhoneNumber(order.getPhoneNumber());
        orderInfoResponseVo.setPrice(order.getPrice());
        orderInfoResponseVo.setRefundStatus(order.getRefundStatus());

        Store store=storeMapper.selectById(order.getStoreId());
        if(store!=null) {
            orderInfoResponseVo.setStoreName(store.getStoreName());
            orderInfoResponseVo.setStoreId(store.getId());
        }

        Product product = productMapper.selectById(order.getProductId());
        if(product!=null){
            orderInfoResponseVo.setProductId(order.getProductId());
            orderInfoResponseVo.setProductName(product.getName());
            orderInfoResponseVo.setProductImagePath(product.getImagePath());
        }

        Sku sku = skuMapper.selectById(order.getSkuId());
        if(sku!=null){
            orderInfoResponseVo.setProductAttribute(sku.getProductAttribute());
        }

        if(order.getCouponId()!=null){
            orderInfoResponseVo.setDiscount(couponMapper.selectById(order.getCouponId()).getDiscount());
        }
        if(order.getStatus()==6){
            queryWrapper.clear();
            queryWrapper.eq("order_id",orderId);
            Comment comment = commentMapper.selectOne(queryWrapper);
            orderInfoResponseVo.setLevel(comment.getLevel());
            orderInfoResponseVo.setContent(comment.getContent());
        }
        return orderInfoResponseVo;
    }

    @Override
    @Transactional
    public PageResponseVo<OrderInfoResponseVo> listOrderByCustomer(Integer customerId,Integer customer_visible, Integer status, Integer pageNum, Integer pageSize){

        QueryWrapper queryWrapper =new QueryWrapper();
        if (customer_visible==1)
        {
            queryWrapper.eq("customer_visible",1);
        }
        if(status==5){
            queryWrapper.eq("customer_id",customerId);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("customer_id", customerId);
            map.put("status", status);
            queryWrapper.allEq(map);
        }

        Page<Order>page = new Page(pageNum,pageSize);
        IPage<Order> orderIPage = orderMapper.selectPage(page,queryWrapper);

        List<Order>orders=orderIPage.getRecords();
        List<OrderInfoResponseVo>orderInfoResponseVos=new ArrayList<>();
        for(int i=0; i<orders.size(); i++){
            Order order=orders.get(i);
            OrderInfoResponseVo orderInfoResponseVo=getOrderInfo(order.getId());
            orderInfoResponseVos.add(orderInfoResponseVo);
        }

        PageResponseVo<OrderInfoResponseVo>orderInfoResponseVoPageResponseVo=new PageResponseVo(orderIPage);
        orderInfoResponseVoPageResponseVo.setData(orderInfoResponseVos);
        return orderInfoResponseVoPageResponseVo;
    }

    @Override
    public void deleteOrder(Integer customerId,Integer orderId){

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("customer_id",customerId);
        queryWrapper.eq("id",orderId);
        Order order = orderMapper.selectOne(queryWrapper);
        order.setCustomerVisible(0);
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public String aliNotify(Map<String, String> conversionParams){

        //????????????(???????????????????????????????????????????????????????????????)
        boolean signVerified = false;
        try {
            //??????SDK????????????
            String alipayPublicKey = AppConst.ALIPAY_PUBLIC_KEY;
            String charset = AppConst.CHARSET;
            String signType = AppConst.SIGN_TYPE;

            signVerified = AlipaySignature.rsaCheckV1(conversionParams, alipayPublicKey, charset, signType);
            //?????????????????????.
            if (signVerified) {

                // ????????????????????????????????????????????????????????????????????????????????????1\2\3\4?????????????????????????????????response?????????success?????????????????????failure ????????????????????????????????????out_trade_no???total_amount???sellerId???app_id???
                //this.check(conversionParams);
                //???????????? ??????????????????
                String tradeStatus = conversionParams.get("trade_status");

                //??????????????????????????????: ?????????????????????,????????????
                //???????????????????????????TRADE_SUCCESS???TRADE_FINISHED???????????????????????????????????????????????????
                if (tradeStatus.equals("TRADE_SUCCESS") ||tradeStatus.equals("TRADE_FINISHED")) {
                    //?????????????????????????????????????????????
                    //???????????? ?????????????????????
                    String orderNumber = conversionParams.get("out_trade_no");

                    /**??????????????????*/
                    QueryWrapper queryWrapper=new QueryWrapper();
                    queryWrapper.eq("order_number",orderNumber);
                    Order order = orderMapper.selectOne(queryWrapper);
                    order.setStatus(1);
                    orderMapper.updateById(order);

                    Product product=productMapper.selectById(order.getProductId());
                    product.setSalesNum(product.getSalesNum()+order.getProductNum());
                    productMapper.updateById(product);

                    logger.info("order_payed->order_number:{},total_amount:{},",new Object[]{orderNumber, conversionParams.get("total_amount")});

                    return "success";
                } else {
                    return "fail";
                    }
            }else{  //???????????????

                return "fail";
            }
        } catch (AlipayApiException e) {

            e.printStackTrace();
        }
            return "fail";

    }

    @Override
    public String payOrder (Integer orderId,HttpServletResponse servletResponse) {

        Order order = orderMapper.selectById(orderId);
        OrderInfoResponseVo orderInfoResponseVo = orderService.getOrderInfo(orderId);

        //?????????????????? AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);

        try {
            //???1?????????bizmodel??????
            AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            //SDK????????????????????????????????????????????????????????????????????????????????????sdk???model????????????(model???biz_content???????????????????????????biz_content)???
            model.setOutTradeNo(order.getOrderNumber());
            model.setSubject(orderInfoResponseVo.getProductName());
            model.setBody(orderInfoResponseVo.getProductName());
           // model.setProductCode("QUICK_WAP_WAY");
            model.setSellerId("2088621956175664");
            model.setTotalAmount(order.getTotalPrice().toString());
            model.setTimeoutExpress("30m");
            //model.setQuitUrl("https://www.hao123.com/");
            //???2?????????????????????
            //alipayRequest.setReturnUrl();
            alipayRequest.setNotifyUrl("http://121.36.58.203:8081/order/order/payNotify");
            //alipayRequest.setReturnUrl("https://www.hao123.com/");
            alipayRequest.setBizModel(model);
            //???3?????????
            String form = alipayClient.execute(alipayRequest).getBody();
            //System.out.println("*********************\n??????????????????" + form);

            JSONObject info = JSONObject.parseObject(form);
            String response = info.getString("alipay_trade_precreate_response");
            JSONObject response_info = JSONObject.parseObject(response);
            String qr_code = response_info.getString("qr_code");

            qrCodeService.createCodeToStream(qr_code,servletResponse);

            return "????????????????????????";
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "????????????????????????";
        }

    }

    @Override
    /**????????????App**/
    public String payOrderByApp (Integer orderId){

        Order order = orderMapper.selectById(orderId);
        OrderInfoResponseVo orderInfoResponseVo = orderService.getOrderInfo(orderId);

        //?????????????????? AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);

        try {
            //???1?????????bizmodel??????
            AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            //SDK????????????????????????????????????????????????????????????????????????????????????sdk???model????????????(model???biz_content???????????????????????????biz_content)???
            model.setOutTradeNo(order.getOrderNumber());
            model.setSubject(orderInfoResponseVo.getProductName());
            model.setBody(orderInfoResponseVo.getProductName());
            // model.setProductCode("QUICK_WAP_WAY");
            model.setSellerId("2088621956175664");
            model.setTotalAmount(order.getTotalPrice().toString());
            model.setTimeoutExpress("30m");
            //model.setQuitUrl("https://www.hao123.com/");
            //???2?????????????????????
            //alipayRequest.setReturnUrl();
            alipayRequest.setNotifyUrl("http://jzikxg.natappfree.cc/order/order/payNotify");
            //alipayRequest.setReturnUrl("https://www.hao123.com/");
            alipayRequest.setBizModel(model);
            //???3?????????
            String form = alipayClient.sdkExecute(alipayRequest).getBody();
            //System.out.println("*********************\n??????????????????" + form);

            return form;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "????????????????????????";
        }

    }

    @Override
    /**????????????Wap**/
    public String payOrderByWap (Integer orderId){

        Order order = orderMapper.selectById(orderId);
        OrderInfoResponseVo orderInfoResponseVo = orderService.getOrderInfo(orderId);

        //?????????????????? AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);
        try {
            //??????bizmodel??????
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            //SDK????????????????????????????????????????????????????????????????????????????????????sdk???model????????????(model???biz_content???????????????????????????biz_content)???
            model.setOutTradeNo(order.getOrderNumber());
            model.setSubject(orderInfoResponseVo.getProductName());
            model.setBody(orderInfoResponseVo.getProductName());
            model.setProductCode("QUICK_WAP_WAY");
            model.setSellerId("2088621956175664");
            model.setTotalAmount(order.getTotalPrice().toString());
            model.setTimeoutExpress("30m");
            model.setQuitUrl("https://www.hao123.com/");

            alipayRequest.setNotifyUrl("http://jzikxg.natappfree.cc/order/order/payNotify");
            //alipayRequest.setReturnUrl("https://www.hao123.com/");
            alipayRequest.setBizModel(model);
            //??????
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            //System.out.println("*********************\n??????????????????" + form);

            return form;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "????????????????????????";
        }


    }

    @Override
    public String payOrderPrecreate (Integer orderId) {

        Order order = orderMapper.selectById(orderId);
        OrderInfoResponseVo orderInfoResponseVo = orderService.getOrderInfo(orderId);

        //?????????????????? AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);

        try {
            //???1?????????bizmodel??????
            AlipayTradeMergePrecreateRequest alipayRequest = new AlipayTradeMergePrecreateRequest();
            AlipayTradeMergePrecreateModel model = new AlipayTradeMergePrecreateModel();
            //SDK????????????????????????????????????????????????????????????????????????????????????sdk???model????????????(model???biz_content???????????????????????????biz_content)???
            OrderDetail orderDetail=new OrderDetail();

            orderDetail.setAppId(AppConst.APP_ID);
            orderDetail.setOutTradeNo(order.getOrderNumber());
            orderDetail.setSubject(orderInfoResponseVo.getProductName());
            orderDetail.setBody(orderInfoResponseVo.getProductName());
            orderDetail.setProductCode("QUICK_MSECURITY_PAY");
            orderDetail.setSellerId("2088621956175664");
            orderDetail.setSellerLogonId("2088621956175664");
            orderDetail.setTotalAmount(order.getTotalPrice().toString());
            List<OrderDetail>orderDetails=new ArrayList<>();
            orderDetails.add(orderDetail);
            model.setOrderDetails(orderDetails);
            model.setTimeoutExpress("30m");
            //model.setQuitUrl("https://www.hao123.com/");
            //???2?????????????????????
            //alipayRequest.setReturnUrl();
            alipayRequest.setNotifyUrl("http://jzikxg.natappfree.cc/order/order/payNotify");
            //alipayRequest.setReturnUrl("https://www.hao123.com/");
            alipayRequest.setBizModel(model);
            //???3?????????
            String form = alipayClient.execute(alipayRequest).getBody();
            System.out.println("*********************\n??????????????????" + form);

            JSONObject info = JSONObject.parseObject(form);
            String response = info.getString("pre_order_no");
            return response;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "????????????????????????";
        }

    }

    @Override
    public String payOrderMerge (String pre_order_no){

        //?????????????????? AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);

        try {
            //???1?????????bizmodel??????
            AlipayTradeAppMergePayRequest alipayRequest = new AlipayTradeAppMergePayRequest();
            AlipayTradeAppMergePayModel model = new AlipayTradeAppMergePayModel();



            model.setPreOrderNo(pre_order_no);
            //model.setQuitUrl("https://www.hao123.com/");
            //???2?????????????????????
            //alipayRequest.setReturnUrl();
            alipayRequest.setNotifyUrl("http://jzikxg.natappfree.cc/order/order/payNotify");
            //alipayRequest.setReturnUrl("https://www.hao123.com/");
            alipayRequest.setBizModel(model);
            //???3?????????
            String form = alipayClient.execute(alipayRequest).getBody();
            System.out.println("*********************\n??????????????????" + form);

            return form;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "????????????????????????";
        }
    }

    @Override
    public Integer getPayStatus(Integer orderId){

        Order order=orderMapper.selectById(orderId);
        return order.getStatus();
    }


    @Override
    public PageResponseVo<OrderInfoResponseVo>listOrdersByScreenConditions(Integer storeId,Integer pageNum,Integer pageSize,String orderNumber,Integer status,String gmtCreate) throws ParseException {

        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.eq("store_id",storeId);
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.orderByAsc("status");
        Page<Order>page=new Page<>(pageNum,pageSize);

        if(orderNumber!=null&&!orderNumber.isEmpty()){
            queryWrapper.eq("order_number",orderNumber);
        }
        if(status!=null){
            queryWrapper.eq("status",status);
        }
        if(gmtCreate!=null&&!gmtCreate.isEmpty()){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(gmtCreate);
            Date date1= new Date(date.getTime()+24*3600*1000);
            queryWrapper.between("gmt_create",date,date1);
        }

        IPage<Order> orderIPage=orderMapper.selectPage(page,queryWrapper);

        List<Order>orders=orderIPage.getRecords();
        List<OrderInfoResponseVo>orderInfoResponseVos=new ArrayList<>();
        for(int i=0; i<orders.size(); i++){
            Order order=orders.get(i);
            OrderInfoResponseVo orderInfoResponseVo=getOrderInfo(order.getId());
            orderInfoResponseVos.add(orderInfoResponseVo);
        }

        PageResponseVo<OrderInfoResponseVo>responseVo=new PageResponseVo(orderIPage);
        responseVo.setData(orderInfoResponseVos);
        return responseVo;
    }

    @Override
    public String shiftOrder(Integer orderId){

        Order order = orderMapper.selectById(orderId);
        if(order.getStatus()!=1){
            return "?????????????????????????????????";
        }
        else {
            order.setStatus(2);
            orderMapper.updateById(order);
            return "????????????";
        }
    }

    @Override
    public String confirmReceipt(Integer orderId){

        Order order = orderMapper.selectById(orderId);
        if(order.getStatus()!=2){
            return "?????????????????????????????????";
        }
        else {
            order.setStatus(3);
            orderMapper.updateById(order);
            return "????????????";
        }
    }

    @Override
    public void cancelOrder(Integer orderId){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",0);
        queryWrapper.eq("id",orderId);

        Order order = orderMapper.selectOne(queryWrapper);
        order.setStatus(4);
        orderMapper.updateById(order);

    }

}
