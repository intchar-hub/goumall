package com.stack.dogcat.gomall.content.responseVo;

import java.math.BigDecimal;
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

    private String imagePath;

    /**
     * 商品最高价格（规格不同，价格不同）
     */
    private BigDecimal highestPrice;


    /**
     * 商品最低价格（规格不同，价格不同）
     */
    private BigDecimal lowestPrice;

    @Override
    public String toString() {
        return "BrowserHistoryQueryResponseVo{" +
                "browserHistoryId=" + browserHistoryId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", imagePath='" + imagePath + '\'' +
                ", highestPrice=" + highestPrice +
                ", lowestPrice=" + lowestPrice +
                '}';
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice) {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
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
