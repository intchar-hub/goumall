package com.stack.dogcat.gomall.order.responseVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/17 20:24
 * @Descrition 一年内每个月的订单量和营业额
 */
public class OrderNumAndIncomeQueryResponseVo {

    private List<Integer> orderNum;

    private List<BigDecimal> income;

    @Override
    public String toString() {
        return "SaleNumAndIncomeQueryResponseVo{" +
                "saleNum=" + orderNum +
                ", income=" + income +
                '}';
    }

    public List<Integer> getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(List<Integer> orderNum) {
        this.orderNum = orderNum;
    }

    public List<BigDecimal> getIncome() {
        return income;
    }

    public void setIncome(List<BigDecimal> income) {
        this.income = income;
    }
}
