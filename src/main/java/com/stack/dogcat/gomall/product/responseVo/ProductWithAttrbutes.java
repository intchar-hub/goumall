package com.stack.dogcat.gomall.product.responseVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/15 19:04
 * @Descrition 用于上架商品、修改商品信息时的JSON字符串解析，示例如下
 * "{\"attrNameIdArray\":[1, 2],\"data\": [{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]},{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]}]}";
 */
public class ProductWithAttrbutes implements Serializable {

    private Integer stockNum;

    private BigDecimal price;

    List<Integer> valueArray;

    @Override
    public String toString() {
        return "ProductWithAttrbutes{" +
                "stockNum=" + stockNum +
                ", price=" + price +
                ", valueArray=" + valueArray +
                '}';
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

    public List<Integer> getValueArray() {
        return valueArray;
    }

    public void setValueArray(List<Integer> valueArray) {
        this.valueArray = valueArray;
    }
}
