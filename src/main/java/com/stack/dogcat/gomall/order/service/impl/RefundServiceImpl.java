package com.stack.dogcat.gomall.order.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.config.AlipayConfig;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.entity.Refund;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.order.mapper.RefundMapper;
import com.stack.dogcat.gomall.order.responseVo.OrderInfoResponseVo;
import com.stack.dogcat.gomall.order.responseVo.RefundListInfo;
import com.stack.dogcat.gomall.order.responseVo.RefundOrderInfo;
import com.stack.dogcat.gomall.order.responseVo.RefundResponseVo;
import com.stack.dogcat.gomall.order.service.IRefundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.utils.AppConst;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 退款表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements IRefundService {

    @Autowired
    RefundMapper refundMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    SkuMapper skuMapper;

    @Override
    @Transactional
    public void saveRefund(Integer orderId,String reason){

        Order order = orderMapper.selectById(orderId);
        order.setRefundStatus(1);

        Refund refund = new Refund();
        refund.setOrderId(orderId);
        refund.setCustomerId(order.getCustomerId());
        refund.setStoreId(order.getStoreId());
        refund.setReason(reason);
        refund.setGmtCreate(LocalDateTime.now());

        orderMapper.updateById(order);
        refundMapper.insert(refund);
    }

    @Override
    public PageResponseVo<RefundResponseVo> listRefundByStoreId(Integer storeId, Integer status, Integer pageNum, Integer pageSize){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id",storeId);
        queryWrapper.orderByDesc("gmt_create");
        if(status==5){
            queryWrapper.in("status", 3,4);
        }else{
            queryWrapper.eq("status",status);
        }

        Page<Refund>page=new Page<>(pageNum,pageSize);
        IPage<Refund>refundIPage=refundMapper.selectPage(page,queryWrapper);
        PageResponseVo<RefundResponseVo>responseVo=new PageResponseVo(refundIPage);
        responseVo.setData(CopyUtil.copyList(refundIPage.getRecords(),RefundResponseVo.class));

        return responseVo;
    }

    @Override
    @Transactional
    public String handleRefundApply(Integer refundId,Integer option) throws InterruptedException {

        Refund refund = refundMapper.selectById(refundId);
        if(option==0){
            refund.setStatus(4);
            Order order = orderMapper.selectById(refund.getOrderId());
            order.setRefundStatus(4);

            orderMapper.updateById(order);
            refundMapper.updateById(refund);
            return "退款申请已拒绝";
        }
        else if(option==1){
            Order order = orderMapper.selectById(refund.getOrderId());
            String refundResponse=aliPayRefund(order.getOrderNumber(),order.getTotalPrice().toString());

            Thread.sleep(10000);
            if(aliPayRefundQuery(order.getOrderNumber())){
                refund.setStatus(2);
                refundMapper.updateById(refund);
                return refundResponse;
            }else{
                return "退款未执行成功，请重新请求";
            }
        }
        return "未定义的操作码";
    }


    @Override
    public String aliPayRefund(String out_trade_no,String refund_amount){

        //获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);

        try {
            //封装bizmodel信息
            AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel refundModel =new AlipayTradeRefundModel();

            refundModel.setOutTradeNo(out_trade_no);
            refundModel.setRefundAmount(refund_amount);

            refundRequest.setBizModel(refundModel);

            AlipayTradeRefundResponse response = alipayClient.execute(refundRequest);

            return response.getBody();

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    @Override
    public boolean aliPayRefundQuery(String out_trade_no){

        //获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AppConst.APP_ID, AppConst.APP_PRIVATE_KEY, AppConst.FORMAT, AppConst.CHARSET, AppConst.ALIPAY_PUBLIC_KEY, AppConst.SIGN_TYPE);

        try {
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();

            model.setOutTradeNo(out_trade_no);
            model.setOutRequestNo(out_trade_no);

            request.setBizModel(model);

            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            System.out.println(response);

            if(response.getOutTradeNo()!=null&&response.getOutRequestNo()!=null&&response.getRefundAmount()!=null){
                return true;
            }else{
                return false;
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    @Transactional
    public void cancelRefund(Integer refundId){

        Refund refund = refundMapper.selectById(refundId);
        refund.setStatus(3);

        Order order = orderMapper.selectById(refund.getOrderId());
        order.setRefundStatus(3);

        refundMapper.updateById(refund);
        orderMapper.updateById(order);
    }

    @Override
    public PageResponseVo<RefundListInfo> listRefundByCustomerId(Integer customerId, Integer pageNum, Integer pageSize){

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("customer_id",customerId);
        queryWrapper.orderByAsc("status");
        queryWrapper.orderByDesc("gmt_create");

        Page<Refund>page=new Page<>(pageNum,pageSize);
        IPage<Refund>refundIPage=refundMapper.selectPage(page,queryWrapper);

        List<Refund>refundList=refundIPage.getRecords();
        List<RefundListInfo>refundListInfos=new ArrayList<>();
        for(int i=0; i<refundList.size(); i++){
            Refund refund = refundList.get(i);
            RefundListInfo refundListInfo =new RefundListInfo();
            refundListInfo.setRefundId(refund.getId());
            refundListInfo.setRefundStatus(refund.getStatus());
            refundListInfo.setGmtCreate(refund.getGmtCreate());

            Order order =orderMapper.selectById(refund.getOrderId());
            refundListInfo.setProductNum(order.getProductNum());

            Product product = productMapper.selectById(order.getProductId());
            refundListInfo.setProductName(product.getName());
            refundListInfo.setProductAvatarPath(product.getImagePath());

            refundListInfos.add(refundListInfo);
        }

        PageResponseVo<RefundListInfo> responseVo = new PageResponseVo(refundIPage);
        responseVo.setData(refundListInfos);

        return responseVo;
    }

    @Override
    public OrderInfoResponseVo getRefundOrderInfo(Integer refundId){

        OrderInfoResponseVo refundOrderInfo=orderService.getOrderInfo(refundMapper.selectById(refundId).getOrderId(),1);
        return refundOrderInfo;
    }

    @Override
    public PageResponseVo<RefundOrderInfo>listRefundByScreenConditions(Integer storeId, Integer pageNum, Integer pageSize, String orderNumber, Integer refundStatus, String gmtCreate) throws ParseException {
/**
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.eq("store_id",storeId);
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.orderByAsc("refund_status");
        Page<Order>page=new Page<>(pageNum,pageSize);

        if(orderNumber!=null&&!orderNumber.isEmpty()){
            queryWrapper.eq("order_number",orderNumber);
        }
        if(refundStatus!=null){
            queryWrapper.eq("refund_status",refundStatus);
        }
        if(refundStatus==null){
            queryWrapper.ne("refund_status",0);
        }
        if(gmtCreate!=null&&!gmtCreate.isEmpty()){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(gmtCreate);
            Date date1= new Date(date.getTime()+24*3600*1000);
            queryWrapper.between("gmt_create",date,date1);
        }

        IPage<Order> orderIPage=orderMapper.selectPage(page,queryWrapper);
        List<Order>orders=orderIPage.getRecords();
        List<RefundOrderInfo>refundOrderInfos=new ArrayList<>();

        for(int i=0; i<orders.size(); i++){
            Order order=orders.get(i);
            RefundOrderInfo rfoInfo= new RefundOrderInfo();
            queryWrapper.clear();
            queryWrapper.eq("order_id",order.getId());
            Refund refund = refundMapper.selectOne(queryWrapper);
            Product product = productMapper.selectById(order.getProductId());

            rfoInfo.setProductName(product.getName());
            rfoInfo.setImagePath(product.getImagePath());
            rfoInfo.setProductAttribute(skuMapper.selectById(order.getSkuId()).getProductAttribute());
            rfoInfo.setRefundId(refund.getId());
            rfoInfo.setOrderId(order.getId());
            rfoInfo.setStoreId(order.getStoreId());
            rfoInfo.setCustomerId(order.getCustomerId());
            rfoInfo.setProductId(order.getProductId());
            rfoInfo.setSkuId(order.getSkuId());
            rfoInfo.setOrderNumber(order.getOrderNumber());
            rfoInfo.setProductNum(order.getProductNum());
            rfoInfo.setPrice(order.getPrice());
            rfoInfo.setStatus(order.getStatus());
            rfoInfo.setRefundStatus(order.getRefundStatus());
            rfoInfo.setReceiveAddressId(order.getReceiveAddressId());
            rfoInfo.setConsignee(order.getConsignee());
            rfoInfo.setPhoneNumber(order.getPhoneNumber());
            rfoInfo.setAddress(order.getAddress());
            rfoInfo.setCouponId(order.getCouponId());
            rfoInfo.setCouponDiscount(order.getCouponDiscount());
            rfoInfo.setTotalPrice(order.getTotalPrice());
            rfoInfo.setReason(refund.getReason());
            rfoInfo.setRefundCreate(refund.getGmtCreate());
            rfoInfo.setOrderCreate(order.getGmtCreate());

            refundOrderInfos.add(rfoInfo);
        }


        PageResponseVo<RefundOrderInfo>responseVo=new PageResponseVo(orderIPage);
        responseVo.setData(refundOrderInfos);
        return responseVo;
    }*/

        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.eq("store_id",storeId);
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.orderByAsc("status");
        if(refundStatus!=null){
            queryWrapper.eq("status",refundStatus);
        }
        if(gmtCreate!=null&&!gmtCreate.isEmpty()){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(gmtCreate);
            Date date1= new Date(date.getTime()+24*3600*1000);
            queryWrapper.between("gmt_create",date,date1);
        }

        Page<Refund>page=new Page<>(pageNum,pageSize);
        IPage<Refund> refundIPage=refundMapper.selectPage(page,queryWrapper);
        List<Refund>refunds=refundIPage.getRecords();

        List<RefundOrderInfo>refundOrderInfos=new ArrayList<>();

        if(orderNumber!=null&&!orderNumber.isEmpty()){
            queryWrapper.clear();
            queryWrapper.eq("order_number",orderNumber);
            Order order=orderMapper.selectOne(queryWrapper);
            queryWrapper.clear();
            queryWrapper.eq("order_id",order.getId());
            Refund refund = refundMapper.selectOne(queryWrapper);
            Product product = productMapper.selectById(order.getProductId());

            RefundOrderInfo rfoInfo= new RefundOrderInfo();
            rfoInfo.setProductName(product.getName());
            rfoInfo.setImagePath(product.getImagePath());
            rfoInfo.setProductAttribute(skuMapper.selectById(order.getSkuId()).getProductAttribute());
            rfoInfo.setRefundId(refund.getId());
            rfoInfo.setOrderId(order.getId());
            rfoInfo.setStoreId(order.getStoreId());
            rfoInfo.setCustomerId(order.getCustomerId());
            rfoInfo.setProductId(order.getProductId());
            rfoInfo.setSkuId(order.getSkuId());
            rfoInfo.setOrderNumber(order.getOrderNumber());
            rfoInfo.setProductNum(order.getProductNum());
            rfoInfo.setPrice(order.getPrice());
            rfoInfo.setStatus(order.getStatus());
            rfoInfo.setRefundStatus(order.getRefundStatus());
            rfoInfo.setReceiveAddressId(order.getReceiveAddressId());
            rfoInfo.setConsignee(order.getConsignee());
            rfoInfo.setPhoneNumber(order.getPhoneNumber());
            rfoInfo.setAddress(order.getAddress());
            rfoInfo.setCouponId(order.getCouponId());
            rfoInfo.setCouponDiscount(order.getCouponDiscount());
            rfoInfo.setTotalPrice(order.getTotalPrice());
            rfoInfo.setReason(refund.getReason());
            rfoInfo.setRefundCreate(refund.getGmtCreate());
            rfoInfo.setOrderCreate(order.getGmtCreate());

            refundOrderInfos.add(rfoInfo);
        }
        else {
            for (int i = 0; i < refunds.size(); i++) {

                Order order = orderMapper.selectById(refunds.get(i).getOrderId());
                Product product = productMapper.selectById(order.getProductId());
                RefundOrderInfo rfoInfo= new RefundOrderInfo();

                rfoInfo.setProductName(product.getName());
                rfoInfo.setImagePath(product.getImagePath());
                rfoInfo.setProductAttribute(skuMapper.selectById(order.getSkuId()).getProductAttribute());
                rfoInfo.setRefundId(refunds.get(i).getId());
                rfoInfo.setOrderId(order.getId());
                rfoInfo.setStoreId(order.getStoreId());
                rfoInfo.setCustomerId(order.getCustomerId());
                rfoInfo.setProductId(order.getProductId());
                rfoInfo.setSkuId(order.getSkuId());
                rfoInfo.setOrderNumber(order.getOrderNumber());
                rfoInfo.setProductNum(order.getProductNum());
                rfoInfo.setPrice(order.getPrice());
                rfoInfo.setStatus(order.getStatus());
                rfoInfo.setRefundStatus(order.getRefundStatus());
                rfoInfo.setReceiveAddressId(order.getReceiveAddressId());
                rfoInfo.setConsignee(order.getConsignee());
                rfoInfo.setPhoneNumber(order.getPhoneNumber());
                rfoInfo.setAddress(order.getAddress());
                rfoInfo.setCouponId(order.getCouponId());
                rfoInfo.setCouponDiscount(order.getCouponDiscount());
                rfoInfo.setTotalPrice(order.getTotalPrice());
                rfoInfo.setReason(refunds.get(i).getReason());
                rfoInfo.setRefundCreate(refunds.get(i).getGmtCreate());
                rfoInfo.setOrderCreate(order.getGmtCreate());

                refundOrderInfos.add(rfoInfo);
            }
        }

        PageResponseVo<RefundOrderInfo>responseVo=new PageResponseVo(refundIPage);
        responseVo.setData(refundOrderInfos);
        return responseVo;
    }



}
