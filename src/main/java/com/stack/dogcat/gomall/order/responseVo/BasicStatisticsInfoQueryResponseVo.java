package com.stack.dogcat.gomall.order.responseVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/17 20:52
 * @Descrition 店铺内各项基本统计信息（参照mall-admin）
 */
public class BasicStatisticsInfoQueryResponseVo {

    /**
     * 订单相关
     */
    private Integer todayOrderNum; // 今日订单数

    private Integer yesterdayOrderNum; // 昨日订单数

    private BigDecimal todayIncome; // 今日营业额

    private BigDecimal yesterdayIncome; // 昨日营业额

    /**
     * 待处理事务
     */
    private Integer unpayOrderNum; // 待付款订单数

    private Integer unSendOrderNum; // 待发货订单数

    private Integer unReceivedOrderNum; // 待确认收货订单数

    private Integer unHandleRefundNum; //待处理退款申请


    /**
     * 商品总览
     */
    private Integer onSaleProductNum; // 参与秒杀活动商品数（正在秒杀和待秒杀）

    private Integer stockLackProductNum; // 库存紧张商品数（库存少于10）

    private Integer stockEmptyProductNum; // 库存为0商品数

    private Integer allProductNum; // 总商品数

    /**
     * 粉丝总览
     */
    private Integer todayIncreaseFansNum; // 今日新增粉丝数

    private Integer yesterdayIncreaseFansNum; // 昨日新增粉丝数

    private Integer thisMonthIncreaseFansNum; // 本月新增粉丝数

    private Integer totalFansNum; // 总粉丝数

    /**
     * 订单统计
     */
    private Integer thisMonthOrderNum; // 本月订单数

    private BigDecimal monthOrderNumsIncreasingRate; // 同比上月订单数上涨率

    private Integer thisWeekOrderNum; // 本周订单数

    private BigDecimal weekOrderNumsIncreasingRate; // 同比上周订单数上涨率

    private BigDecimal thisMonthIncome; // 本月销售额

    private BigDecimal monthIncomeIncreasingRate; // 同比上月销售额上涨率

    private BigDecimal thisWeekIncome; // 本周销售额

    private BigDecimal weekIncomeIncreasingRate; // 同比上周销售额上涨率

    /**
     * 供商家选择可查看销售情况的年份
     */
    private List<String> years; // 店铺中的订单的所有年份（即该店铺从入驻至今）

    /**
     * 饼图 商品-销售额
     * @return
     */
    private List<ProductWithSalesVolume> productWithSalesVolumes;

    /**
     * 饼图 商品-销售量
     * @return
     */
    private List<ProductWithSalesNum> productWithSalesNums;

    public Integer getTodayOrderNum() {
        return todayOrderNum;
    }

    public void setTodayOrderNum(Integer todayOrderNum) {
        this.todayOrderNum = todayOrderNum;
    }

    public Integer getYesterdayOrderNum() {
        return yesterdayOrderNum;
    }

    public void setYesterdayOrderNum(Integer yesterdayOrderNum) {
        this.yesterdayOrderNum = yesterdayOrderNum;
    }

    public BigDecimal getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(BigDecimal todayIncome) {
        this.todayIncome = todayIncome;
    }

    public BigDecimal getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(BigDecimal yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public Integer getUnpayOrderNum() {
        return unpayOrderNum;
    }

    public void setUnpayOrderNum(Integer unpayOrderNum) {
        this.unpayOrderNum = unpayOrderNum;
    }

    public Integer getUnSendOrderNum() {
        return unSendOrderNum;
    }

    public void setUnSendOrderNum(Integer unSendOrderNum) {
        this.unSendOrderNum = unSendOrderNum;
    }

    public Integer getUnReceivedOrderNum() {
        return unReceivedOrderNum;
    }

    public void setUnReceivedOrderNum(Integer unReceivedOrderNum) {
        this.unReceivedOrderNum = unReceivedOrderNum;
    }

    public Integer getUnHandleRefundNum() {
        return unHandleRefundNum;
    }

    public void setUnHandleRefundNum(Integer unHandleRefundNum) {
        this.unHandleRefundNum = unHandleRefundNum;
    }

    public Integer getOnSaleProductNum() {
        return onSaleProductNum;
    }

    public void setOnSaleProductNum(Integer onSaleProductNum) {
        this.onSaleProductNum = onSaleProductNum;
    }

    public Integer getStockLackProductNum() {
        return stockLackProductNum;
    }

    public void setStockLackProductNum(Integer stockLackProductNum) {
        this.stockLackProductNum = stockLackProductNum;
    }

    public Integer getStockEmptyProductNum() {
        return stockEmptyProductNum;
    }

    public void setStockEmptyProductNum(Integer stockEmptyProductNum) {
        this.stockEmptyProductNum = stockEmptyProductNum;
    }

    public Integer getAllProductNum() {
        return allProductNum;
    }

    public void setAllProductNum(Integer allProductNum) {
        this.allProductNum = allProductNum;
    }

    public Integer getTodayIncreaseFansNum() {
        return todayIncreaseFansNum;
    }

    public void setTodayIncreaseFansNum(Integer todayIncreaseFansNum) {
        this.todayIncreaseFansNum = todayIncreaseFansNum;
    }

    public Integer getYesterdayIncreaseFansNum() {
        return yesterdayIncreaseFansNum;
    }

    public void setYesterdayIncreaseFansNum(Integer yesterdayIncreaseFansNum) {
        this.yesterdayIncreaseFansNum = yesterdayIncreaseFansNum;
    }

    public Integer getThisMonthIncreaseFansNum() {
        return thisMonthIncreaseFansNum;
    }

    public void setThisMonthIncreaseFansNum(Integer thisMonthIncreaseFansNum) {
        this.thisMonthIncreaseFansNum = thisMonthIncreaseFansNum;
    }

    public Integer getTotalFansNum() {
        return totalFansNum;
    }

    public void setTotalFansNum(Integer totalFansNum) {
        this.totalFansNum = totalFansNum;
    }

    public Integer getThisMonthOrderNum() {
        return thisMonthOrderNum;
    }

    public void setThisMonthOrderNum(Integer thisMonthOrderNum) {
        this.thisMonthOrderNum = thisMonthOrderNum;
    }

    public BigDecimal getMonthOrderNumsIncreasingRate() {
        return monthOrderNumsIncreasingRate;
    }

    public void setMonthOrderNumsIncreasingRate(BigDecimal monthOrderNumsIncreasingRate) {
        this.monthOrderNumsIncreasingRate = monthOrderNumsIncreasingRate;
    }

    public Integer getThisWeekOrderNum() {
        return thisWeekOrderNum;
    }

    public void setThisWeekOrderNum(Integer thisWeekOrderNum) {
        this.thisWeekOrderNum = thisWeekOrderNum;
    }

    public BigDecimal getWeekOrderNumsIncreasingRate() {
        return weekOrderNumsIncreasingRate;
    }

    public void setWeekOrderNumsIncreasingRate(BigDecimal weekOrderNumsIncreasingRate) {
        this.weekOrderNumsIncreasingRate = weekOrderNumsIncreasingRate;
    }

    public BigDecimal getThisMonthIncome() {
        return thisMonthIncome;
    }

    public void setThisMonthIncome(BigDecimal thisMonthIncome) {
        this.thisMonthIncome = thisMonthIncome;
    }

    public BigDecimal getMonthIncomeIncreasingRate() {
        return monthIncomeIncreasingRate;
    }

    public void setMonthIncomeIncreasingRate(BigDecimal monthIncomeIncreasingRate) {
        this.monthIncomeIncreasingRate = monthIncomeIncreasingRate;
    }

    public BigDecimal getThisWeekIncome() {
        return thisWeekIncome;
    }

    public void setThisWeekIncome(BigDecimal thisWeekIncome) {
        this.thisWeekIncome = thisWeekIncome;
    }

    public BigDecimal getWeekIncomeIncreasingRate() {
        return weekIncomeIncreasingRate;
    }

    public void setWeekIncomeIncreasingRate(BigDecimal weekIncomeIncreasingRate) {
        this.weekIncomeIncreasingRate = weekIncomeIncreasingRate;
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public List<ProductWithSalesVolume> getProductWithSalesVolumes() {
        return productWithSalesVolumes;
    }

    public void setProductWithSalesVolumes(List<ProductWithSalesVolume> productWithSalesVolumes) {
        this.productWithSalesVolumes = productWithSalesVolumes;
    }

    public List<ProductWithSalesNum> getProductWithSalesNums() {
        return productWithSalesNums;
    }

    public void setProductWithSalesNums(List<ProductWithSalesNum> productWithSalesNums) {
        this.productWithSalesNums = productWithSalesNums;
    }

    @Override
    public String toString() {
        return "BasicStatisticsInfoQueryResponseVo{" +
                "todayOrderNum=" + todayOrderNum +
                ", yesterdayOrderNum=" + yesterdayOrderNum +
                ", todayIncome=" + todayIncome +
                ", yesterdayIncome=" + yesterdayIncome +
                ", unpayOrderNum=" + unpayOrderNum +
                ", unSendOrderNum=" + unSendOrderNum +
                ", unReceivedOrderNum=" + unReceivedOrderNum +
                ", unHandleRefundNum=" + unHandleRefundNum +
                ", onSaleProductNum=" + onSaleProductNum +
                ", stockLackProductNum=" + stockLackProductNum +
                ", stockEmptyProductNum=" + stockEmptyProductNum +
                ", allProductNum=" + allProductNum +
                ", todayIncreaseFansNum=" + todayIncreaseFansNum +
                ", yesterdayIncreaseFansNum=" + yesterdayIncreaseFansNum +
                ", thisMonthIncreaseFansNum=" + thisMonthIncreaseFansNum +
                ", totalFansNum=" + totalFansNum +
                ", thisMonthOrderNum=" + thisMonthOrderNum +
                ", monthOrderNumsIncreasingRate=" + monthOrderNumsIncreasingRate +
                ", thisWeekOrderNum=" + thisWeekOrderNum +
                ", weekOrderNumsIncreasingRate=" + weekOrderNumsIncreasingRate +
                ", thisMonthIncome=" + thisMonthIncome +
                ", monthIncomeIncreasingRate=" + monthIncomeIncreasingRate +
                ", thisWeekIncome=" + thisWeekIncome +
                ", weekIncomeIncreasingRate=" + weekIncomeIncreasingRate +
                ", years=" + years +
                ", productWithSalesVolumes=" + productWithSalesVolumes +
                ", productWithSalesNums=" + productWithSalesNums +
                '}';
    }
}
