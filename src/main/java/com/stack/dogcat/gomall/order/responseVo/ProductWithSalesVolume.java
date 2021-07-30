package com.stack.dogcat.gomall.order.responseVo;

import java.math.BigDecimal;

/**
 * @Author Yang Jie
 * @Date 2021/7/28 8:34
 * @Descrition TODO
 */
public class ProductWithSalesVolume {

    private String name; //商品名

    private BigDecimal value; //销售额

    @Override
    public String toString() {
        return "ProductWithSalesVolume{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
