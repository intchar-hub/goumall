package com.stack.dogcat.gomall.product.responseVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductQueryResponseVo {

    private Integer id;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 添加商品时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 库存量
     */
    private Integer stockNum;

    /**
     * 商品展示图片路径
     */
    private String imagePath;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 销量
     */
    private Integer salesNum;

    /**
     * 商品类型id
     */
    private Integer typeId;

    /**
     * 商品的用户点击量
     */
    private Integer clickNum;

    /**
     * 商品最高价格（规格不同，价格不同）
     */
    private BigDecimal highestPrice;


    /**
     * 商品最低价格（规格不同，价格不同）
     */
    private BigDecimal lowestPrice;

    /**
     * 是否参加秒杀活动：0->不参加， 1->参加
     */
    private Integer isOnsale;

    private Integer status;

    @Override
    public String toString() {
        return "ProductQueryResponseVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", storeId=" + storeId +
                ", gmtCreate=" + gmtCreate +
                ", stockNum=" + stockNum +
                ", imagePath='" + imagePath + '\'' +
                ", description='" + description + '\'' +
                ", salesNum=" + salesNum +
                ", typeId=" + typeId +
                ", clickNum=" + clickNum +
                ", highestPrice=" + highestPrice +
                ", lowestPrice=" + lowestPrice +
                ", isOnsale=" + isOnsale +
                ", status=" + status +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(Integer salesNum) {
        this.salesNum = salesNum;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getClickNum() {
        return clickNum;
    }

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
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

    public Integer getIsOnsale() {
        return isOnsale;
    }

    public void setIsOnsale(Integer isOnsale) {
        this.isOnsale = isOnsale;
    }
}
