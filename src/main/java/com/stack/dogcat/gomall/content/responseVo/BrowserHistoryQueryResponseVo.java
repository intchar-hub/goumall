package com.stack.dogcat.gomall.content.responseVo;

import java.time.LocalDateTime;

/**
 * @Author Yang Jie
 * @Date 2021/7/13 8:50
 * @Descrition TODO
 */
public class BrowserHistoryQueryResponseVo {

    private Integer browserHistoryId;

    private Integer productId;

    private String productName;

    private LocalDateTime gmtCreate;

    @Override
    public String toString() {
        return "BrowserHistoryQueryResponseVo{" +
                "browserHistoryId=" + browserHistoryId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getBrowserHistoryId() {
        return browserHistoryId;
    }

    public void setBrowserHistoryId(Integer browserHistoryId) {
        this.browserHistoryId = browserHistoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
