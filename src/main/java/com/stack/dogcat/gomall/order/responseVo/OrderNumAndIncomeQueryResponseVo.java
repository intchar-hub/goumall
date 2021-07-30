package com.stack.dogcat.gomall.order.responseVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/17 20:24
 * @Descrition 一年内每个月的订单量和营业额
 */
public class OrderNumAndIncomeQueryResponseVo {

    private String date;

    private Integer orderCount;

    private BigDecimal orderAmount;

    @Override
    public String toString() {
        return "OrderNumAndIncomeQueryResponseVo{" +
                "date='" + date + '\'' +
                ", orderCount=" + orderCount +
                ", orderAmount=" + orderAmount +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}
