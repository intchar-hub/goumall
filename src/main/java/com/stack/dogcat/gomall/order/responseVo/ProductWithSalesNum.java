package com.stack.dogcat.gomall.order.responseVo;

/**
 * @Author Yang Jie
 * @Date 2021/7/28 14:08
 * @Descrition TODO
 */
public class ProductWithSalesNum {

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ProductWithSalesNum{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
