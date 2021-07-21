package com.stack.dogcat.gomall.product.requestVo;

import java.math.BigDecimal;

/**
 * @Author Yang Jie
 * @Date 2021/7/21 22:47
 * @Descrition TODO
 */
public class SkuUpdateRequestVo {

    private Integer id;

    private Integer stockNum;

    private BigDecimal price;

    @Override
    public String toString() {
        return "SkuUpdateRequestVo{" +
                "id=" + id +
                ", stockNum=" + stockNum +
                ", price=" + price +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
