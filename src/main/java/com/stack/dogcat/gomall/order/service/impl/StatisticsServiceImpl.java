package com.stack.dogcat.gomall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.content.mapper.StoreCollectionMapper;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.order.mapper.RefundMapper;
import com.stack.dogcat.gomall.order.mapper.StatisticsMapper;
import com.stack.dogcat.gomall.order.responseVo.BasicStatisticsInfoQueryResponseVo;
import com.stack.dogcat.gomall.order.responseVo.OrderNumAndIncomeQueryResponseVo;
import com.stack.dogcat.gomall.order.responseVo.ProductWithSalesNum;
import com.stack.dogcat.gomall.order.responseVo.ProductWithSalesVolume;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.sales.mapper.SalesPromotionMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/17 16:31
 * @Descrition 处理店铺的统计信息相关业务
 */
@Service
public class StatisticsServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    SalesPromotionMapper salesPromotionMapper;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    StoreCollectionMapper storeCollectionMapper;

    @Autowired
    StatisticsMapper statisticsMapper;

    @Autowired
    RefundMapper refundMapper;

    /**
     * 商家按年份查询每月的销售额
     * @param storeId
     * @param year
     * @return
     */
    public List<OrderNumAndIncomeQueryResponseVo> listOrderNumAndIncomeByYear(Integer storeId, String year) {
        if(storeId == null || year == null || year.isEmpty()) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", storeId);
        queryWrapper.eq("status", 3);
        queryWrapper.likeRight("gmt_create", year);

        List<Order> ordersFinished = orderMapper.selectList(queryWrapper);

        Integer[] orderNum = new Integer[12]; // 订单数
        BigDecimal[] income = new BigDecimal[12];
        for (int i = 0; i < 12; i++) {
            orderNum[i] = new Integer(0);
            income[i] = new BigDecimal(0.0);
        }

        for (Order order : ordersFinished) {
            orderNum[order.getGmtCreate().getMonthValue() - 1]++;
            income[order.getGmtCreate().getMonthValue() - 1] = income[order.getGmtCreate().getMonthValue() - 1].add(order.getTotalPrice());
        }

        List<OrderNumAndIncomeQueryResponseVo> responseVos = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            OrderNumAndIncomeQueryResponseVo vo = new OrderNumAndIncomeQueryResponseVo();
            vo.setDate((i + 1) + "");
            vo.setOrderCount(orderNum[i]);
            vo.setOrderAmount(income[i]);

            responseVos.add(vo);
        }

        return responseVos;
//
//        OrderNumAndIncomeQueryResponseVo responseVo = new OrderNumAndIncomeQueryResponseVo();
//        responseVo.setIncome(Arrays.asList(income));
//        responseVo.setOrderNum(Arrays.asList(orderNum));

//        return responseVo;
    }

    /**
     * 商家按月份查询每天的销售额
     * @param storeId
     * @param month
     * @return
     */
    public List<OrderNumAndIncomeQueryResponseVo> listOrderNumAndIncomeByMonth(Integer storeId, String month) {
        if(storeId == null || month == null || month.isEmpty()) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        List<OrderNumAndIncomeQueryResponseVo> responseVos = statisticsMapper.selectOrderNumAndIncomeByMonth(storeId, month + "%");

        return responseVos;
    }

    /**
     * 商家查看各项基本统计信息
     * @param storeId
     * @return
     */
    public BasicStatisticsInfoQueryResponseVo listBasicStatisticsInfo(Integer storeId) {
        if(storeId == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        LocalDateTime now = LocalDateTime.now();

        /**
         * 订单相关
         */
        Integer todayOrderNum; // 今日订单数
        Integer yesterdayOrderNum; // 昨日订单数
        BigDecimal todayIncome = new BigDecimal(0.0); // 今日营业额
        BigDecimal yesterdayIncome = new BigDecimal(0.0); // 昨日营业额

        QueryWrapper orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("store_id", storeId);
        orderQueryWrapper.eq("status", 3);
        orderQueryWrapper.likeRight("gmt_create", now.toString().substring(0, 10));
        List<Order> ordersToday = orderMapper.selectList(orderQueryWrapper);
        todayOrderNum = ordersToday.size();
        for (Order order : ordersToday) {
            todayIncome = todayIncome.add(order.getTotalPrice());
        }

        orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("store_id", storeId);
        orderQueryWrapper.eq("status", 3);
        orderQueryWrapper.likeRight("gmt_create", now.minusDays(1).toString().substring(0, 10));
        List<Order> ordersYesterday = orderMapper.selectList(orderQueryWrapper);
        yesterdayOrderNum = ordersYesterday.size();
        for (Order order : ordersYesterday) {
            yesterdayIncome = yesterdayIncome.add(order.getTotalPrice());
        }

        /**
         * 待处理事务
         */
        Integer unpayOrderNum; // 待付款订单数
        Integer unSendOrderNum; // 待发货订单数
        Integer unReceivedOrderNum; // 待确认收货订单数
        Integer unHandleRefundNum; //待处理退款申请

        orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("store_id", storeId);
        orderQueryWrapper.eq("status", 0);
        unpayOrderNum = orderMapper.selectCount(orderQueryWrapper);

        orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("store_id", storeId);
        orderQueryWrapper.eq("status", 2);
        unReceivedOrderNum = orderMapper.selectCount(orderQueryWrapper);

        orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("store_id", storeId);
        orderQueryWrapper.eq("status", 1);
        unSendOrderNum = orderMapper.selectCount(orderQueryWrapper);

        QueryWrapper refundQueryWrapper = new QueryWrapper();
        refundQueryWrapper.eq("store_id", storeId);
        refundQueryWrapper.eq("status", 1);
        unHandleRefundNum = refundMapper.selectCount(refundQueryWrapper);


        /**
         * 商品总览
         */
        Integer onSaleProductNum; // 参与秒杀活动商品数（正在秒杀和待秒杀）
        Integer stockLackProductNum; // 库存紧张商品数（库存少于10）
        Integer stockEmptyProductNum; // 库存为0商品数
        Integer allProductNum; // 总商品数

        QueryWrapper productQueryWrapper = new QueryWrapper();
        productQueryWrapper.eq("store_id", storeId);
        allProductNum = productMapper.selectCount(productQueryWrapper);

        productQueryWrapper = new QueryWrapper();
        productQueryWrapper.eq("store_id", storeId);
        productQueryWrapper.eq("stock_num", 0);
        stockEmptyProductNum = productMapper.selectCount(productQueryWrapper);

        productQueryWrapper = new QueryWrapper();
        productQueryWrapper.eq("store_id", storeId);
        productQueryWrapper.lt("stock_num", 10);
        stockLackProductNum = productMapper.selectCount(productQueryWrapper);

        QueryWrapper salesPromotionQueryWrapper = new QueryWrapper();
        salesPromotionQueryWrapper.eq("store_id", storeId);
        salesPromotionQueryWrapper.gt("deadline", LocalDateTime.now());
        onSaleProductNum = salesPromotionMapper.selectCount(salesPromotionQueryWrapper);

        /**
         * 粉丝总览
         */
        Integer todayIncreaseFansNum; // 今日新增粉丝数
        Integer yesterdayIncreaseFansNum; // 昨日新增粉丝数
        Integer thisMonthIncreaseFansNum; // 本月新增粉丝数
        Integer totalFansNum; // 总粉丝数

        QueryWrapper storeCollectionQueryWrapper = new QueryWrapper();
        storeCollectionQueryWrapper.eq("store_id", storeId);
        totalFansNum = storeCollectionMapper.selectCount(storeCollectionQueryWrapper);

        storeCollectionQueryWrapper = new QueryWrapper();
        storeCollectionQueryWrapper.eq("store_id", storeId);
        storeCollectionQueryWrapper.likeRight("gmt_create", now.toString().substring(0, 10));
        todayIncreaseFansNum = storeCollectionMapper.selectCount(storeCollectionQueryWrapper);

        storeCollectionQueryWrapper = new QueryWrapper();
        storeCollectionQueryWrapper.eq("store_id", storeId);
        storeCollectionQueryWrapper.likeRight("gmt_create", now.minusDays(1).toString().substring(0, 10));
        yesterdayIncreaseFansNum = storeCollectionMapper.selectCount(storeCollectionQueryWrapper);

        storeCollectionQueryWrapper = new QueryWrapper();
        storeCollectionQueryWrapper.eq("store_id", storeId);
        storeCollectionQueryWrapper.likeRight("gmt_create", now.toString().substring(0, 7));
        thisMonthIncreaseFansNum = storeCollectionMapper.selectCount(storeCollectionQueryWrapper);

        /**
         * 订单统计
         */
        Integer thisMonthOrderNum; // 本月订单数
        BigDecimal monthOrderNumsIncreasingRate; // 同比上月订单数上涨率
        Integer thisWeekOrderNum; // 本周订单数
        BigDecimal weekOrderNumsIncreasingRate; // 同比上周订单数上涨率
        BigDecimal thisMonthIncome; // 本月销售额
        BigDecimal monthIncomeIncreasingRate; // 同比上月销售额上涨率
        BigDecimal thisWeekIncome; // 本周销售额
        BigDecimal weekIncomeIncreasingRate; // 同比上周销售额上涨率

        orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("store_id", storeId);
        orderQueryWrapper.eq("status", 3);
        orderQueryWrapper.likeRight("gmt_create", now.toString().substring(0, 7));
        thisMonthOrderNum = orderMapper.selectCount(orderQueryWrapper);

        orderQueryWrapper = new QueryWrapper();
        orderQueryWrapper.eq("store_id", storeId);
        orderQueryWrapper.eq("status", 3);
        orderQueryWrapper.likeRight("gmt_create", now.minusMonths(1).toString().substring(0, 7));
        Integer lastMonthOrderNum = orderMapper.selectCount(orderQueryWrapper);
        if(lastMonthOrderNum == 0) {
            monthOrderNumsIncreasingRate = new BigDecimal(thisMonthOrderNum);
        } else {
            monthOrderNumsIncreasingRate = new BigDecimal((thisMonthOrderNum - lastMonthOrderNum) / lastMonthOrderNum);
        }

        thisWeekOrderNum = statisticsMapper.selectThisWeekOrderNum(storeId);
        thisWeekOrderNum = (thisWeekOrderNum == null)? 0: thisWeekOrderNum;
        Integer lastWeekOrderNum = statisticsMapper.selectLastWeekOrderNum(storeId);
        lastWeekOrderNum = (lastWeekOrderNum == null)? 0: lastWeekOrderNum;
        if(lastWeekOrderNum == 0) {
            weekOrderNumsIncreasingRate = new BigDecimal(thisWeekOrderNum);
        } else {
            weekOrderNumsIncreasingRate = new BigDecimal((thisWeekOrderNum - lastWeekOrderNum) / lastWeekOrderNum);
        }

        thisMonthIncome = statisticsMapper.selectThisMonthIncome(storeId);
        thisMonthIncome = (thisMonthIncome == null)? new BigDecimal(0): thisMonthIncome;
        BigDecimal lastMonthIncome = statisticsMapper.selectLastMonthIncome(storeId);
        lastMonthIncome = (lastMonthIncome == null)? new BigDecimal(0): lastMonthIncome;
        if(lastMonthIncome.compareTo(new BigDecimal(0)) == 0) {
            monthIncomeIncreasingRate = thisMonthIncome;
        } else {
            monthIncomeIncreasingRate = thisMonthIncome.subtract(lastMonthIncome).divide(lastMonthIncome);
        }

        thisWeekIncome = statisticsMapper.selectThisWeekIncome(storeId);
        thisWeekIncome = (thisWeekIncome == null)? new BigDecimal(0): thisWeekIncome;
        BigDecimal lastWeekIncome = statisticsMapper.selectLastWeekIncome(storeId);
        lastWeekIncome = (lastWeekIncome == null)? new BigDecimal(0): lastWeekIncome;
        if(lastWeekIncome.compareTo(new BigDecimal(0)) == 0) {
            weekIncomeIncreasingRate = thisWeekIncome;
        } else {
            weekIncomeIncreasingRate = thisWeekIncome.subtract(lastWeekIncome).divide(lastWeekIncome);
        }

        /**
         * 年份
         */
        List<String> years = statisticsMapper.listOrderYears(storeId);

        /**
         * 商品-销售额
         */
        List<ProductWithSalesVolume> productWithSalesVolumes = statisticsMapper.listProductSalesVolume(storeId);

        /**
         * 商品-销售量
         */
        List<ProductWithSalesNum> productWithSalesNums = statisticsMapper.listProductSalesNum(storeId);


        /**
         * 构造返回值
         */
        BasicStatisticsInfoQueryResponseVo responseVo = new BasicStatisticsInfoQueryResponseVo();

        responseVo.setTodayOrderNum(todayOrderNum);
        responseVo.setYesterdayOrderNum(yesterdayOrderNum);
        responseVo.setTodayIncome(todayIncome);
        responseVo.setYesterdayIncome(yesterdayIncome);

        responseVo.setUnpayOrderNum(unpayOrderNum);
        responseVo.setUnSendOrderNum(unSendOrderNum);
        responseVo.setUnReceivedOrderNum(unReceivedOrderNum);
        responseVo.setUnHandleRefundNum(unHandleRefundNum);

        responseVo.setOnSaleProductNum(onSaleProductNum);
        responseVo.setStockLackProductNum(stockLackProductNum);
        responseVo.setStockEmptyProductNum(stockEmptyProductNum);
        responseVo.setAllProductNum(allProductNum);

        responseVo.setTodayIncreaseFansNum(todayIncreaseFansNum);
        responseVo.setYesterdayIncreaseFansNum(yesterdayIncreaseFansNum);
        responseVo.setThisMonthIncreaseFansNum(thisMonthIncreaseFansNum);
        responseVo.setTotalFansNum(totalFansNum);

        responseVo.setThisMonthOrderNum(thisMonthOrderNum);
        responseVo.setMonthOrderNumsIncreasingRate(monthOrderNumsIncreasingRate);
        responseVo.setThisWeekOrderNum(thisWeekOrderNum);
        responseVo.setWeekOrderNumsIncreasingRate(weekOrderNumsIncreasingRate);
        responseVo.setThisMonthIncome(thisMonthIncome);
        responseVo.setMonthIncomeIncreasingRate(monthIncomeIncreasingRate);
        responseVo.setThisWeekIncome(thisWeekIncome);
        responseVo.setWeekIncomeIncreasingRate(weekIncomeIncreasingRate);

        responseVo.setYears(years);

        responseVo.setProductWithSalesVolumes(productWithSalesVolumes);

        responseVo.setProductWithSalesNums(productWithSalesNums);

        return responseVo;
    }
}
