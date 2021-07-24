package com.stack.dogcat.gomall.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.stack.dogcat.gomall.QrCode.QrCodeService;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.config.AlipayConfig;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 订单表 服务实现类
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

    @Override
    @Transactional
    public SysResult saveOrder(Integer customerId, OrderRequestVo orderRequestVo){


        /**判断库存**/
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
            order.setPrice(sku.getPrice());
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

            /**减库存**/
            sku.setStockNum(sku.getStockNum()-orderRequestVo.getProductNum());
            skuMapper.updateById(sku);

            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("order_number",randNum);
            Integer id= orderMapper.selectOne(queryWrapper1).getId();

            SysResult result=SysResult.build(200,"下单成功",id);
            return result;


        }else {
            return SysResult.error("库存不够，下单失败");
        }
    }

    @Override
    @Transactional
    public OrderInfoResponseVo getOrderInfo(Integer orderId,Integer customer_visible){

        OrderInfoResponseVo  orderInfoResponseVo  = new OrderInfoResponseVo ();

        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.eq("id",orderId);
        if(customer_visible == 1) {
            queryWrapper.eq("customer_visible", 1);
        }
        Order order = orderMapper.selectOne(queryWrapper);

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
        }

        Product product = productMapper.selectById(order.getProductId());
        if(product!=null){
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
        return orderInfoResponseVo;
    }

    @Override
    @Transactional
    public PageResponseVo<OrderInfoResponseVo> listOrderByCustomer(Integer customerId, Integer status, Integer pageNum, Integer pageSize){

        QueryWrapper queryWrapper =new QueryWrapper();
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
            OrderInfoResponseVo orderInfoResponseVo=getOrderInfo(order.getId(),1);
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

        //签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
        boolean signVerified = false;
        try {
            //调用SDK验证签名
            String alipayPublicKey = AppConst.ALIPAY_PUBLIC_KEY;
            String charset = AppConst.CHARSET;
            String signType = AppConst.SIGN_TYPE;

            signVerified = AlipaySignature.rsaCheckV1(conversionParams, alipayPublicKey, charset, signType);
            //对验签进行处理.
            if (signVerified) {

                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure 支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）
                //this.check(conversionParams);
                //验签通过 获取交易状态
                String tradeStatus = conversionParams.get("trade_status");

                //只处理支付成功的订单: 修改交易表状态,支付成功
                //只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
                if (tradeStatus.equals("TRADE_SUCCESS") ||tradeStatus.equals("TRADE_FINISHED")) {
                    //获取支付宝通知完成充值后续业务
                    //交易成功 获取商户订单号
                    String orderNumber = conversionParams.get("out_trade_no");

                    /**修改订单信息*/
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
            }else{  //验签不通过

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
        OrderInfoResponseVo orderInfoResponseVo = orderService.getOrderInfo(orderId, 1);

        //获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);

        try {
            //（1）封装bizmodel信息
            AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            model.setOutTradeNo(order.getOrderNumber());
            model.setSubject(orderInfoResponseVo.getProductName());
            model.setBody(orderInfoResponseVo.getProductName());
           // model.setProductCode("QUICK_WAP_WAY");
            model.setSellerId("2088621956175664");
            model.setTotalAmount(order.getTotalPrice().toString());
            model.setTimeoutExpress("30m");
            //model.setQuitUrl("https://www.hao123.com/");
            //（2）设置请求参数
            //alipayRequest.setReturnUrl();
            alipayRequest.setNotifyUrl("http://hqqjw9.natappfree.cc/order/order/payNotify");
            //alipayRequest.setReturnUrl("https://www.hao123.com/");
            alipayRequest.setBizModel(model);
            //（3）请求
            String form = alipayClient.execute(alipayRequest).getBody();
            //System.out.println("*********************\n返回结果为：" + form);

            JSONObject info = JSONObject.parseObject(form);
            String response = info.getString("alipay_trade_precreate_response");
            JSONObject response_info = JSONObject.parseObject(response);
            String qr_code = response_info.getString("qr_code");

            qrCodeService.createCodeToStream(qr_code,servletResponse);

            return "请求支付操作成功";
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "请求支付操作失败";
        }

    }

}
