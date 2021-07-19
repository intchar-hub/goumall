package com.stack.dogcat.gomall.order.responseVo;

import java.math.BigDecimal;

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

    private Integer thismonthIncreaseFansNum; // 本月新增粉丝数

    private Integer totalFansNum; // 总粉丝数

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
                ", onSaleProductNum=" + onSaleProductNum +
                ", stockLackProductNum=" + stockLackProductNum +
                ", stockEmptyProductNum=" + stockEmptyProductNum +
                ", allProductNum=" + allProductNum +
                ", todayIncreaseFansNum=" + todayIncreaseFansNum +
                ", yesterdayIncreaseFansNum=" + yesterdayIncreaseFansNum +
                ", thismonthIncreaseFansNum=" + thismonthIncreaseFansNum +
                ", totalFansNum=" + totalFansNum +
                '}';
    }

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

    public Integer getThismonthIncreaseFansNum() {
        return thismonthIncreaseFansNum;
    }

    public void setThismonthIncreaseFansNum(Integer thismonthIncreaseFansNum) {
        this.thismonthIncreaseFansNum = thismonthIncreaseFansNum;
    }

    public Integer getTotalFansNum() {
        return totalFansNum;
    }

    public void setTotalFansNum(Integer totalFansNum) {
        this.totalFansNum = totalFansNum;
    }
}