package com.stack.dogcat.gomall.sales.responseVo;

/**
 * @Author Yang Jie
 * @Date 2021/7/22 21:44
 * @Descrition TODO
 */
public class SalesProductQueryResponseVo {

    private Integer productId;

    private String productImagepath;

    @Override
    public String toString() {
        return "SalesProductQueryResponseVo{" +
                "productId=" + productId +
                ", productImagepath='" + productImagepath + '\'' +
                '}';
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductImagepath() {
        return productImagepath;
    }

    public void setProductImagepath(String productImagepath) {
        this.productImagepath = productImagepath;
    }
}
