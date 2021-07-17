package com.stack.dogcat.gomall.product.responseVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SkuQueryResponseVo implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    private Integer productId;

    /**
     * 规格属性集合，json表示
     */
    private String productAttribute;

    /**
     * 库存量
     */
    private Integer stockNum;

    /**
     * 该规格下的商品价格
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 该规格的商品销量
     */
    private Integer salesNum;

    @Override
    public String toString() {
        return "SkuQueryResponseVo{" +
                "id=" + id +
                ", productId=" + productId +
                ", productAttribute='" + productAttribute + '\'' +
                ", stockNum=" + stockNum +
                ", price=" + price +
                ", gmtCreate=" + gmtCreate +
                ", salesNum=" + salesNum +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(String productAttribute) {
        this.productAttribute = productAttribute;
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

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(Integer salesNum) {
        this.salesNum = salesNum;
    }
}
