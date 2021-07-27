package com.stack.dogcat.gomall.sales.requestVo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author Yang Jie
 * @Date 2021/7/13 14:57
 * @Descrition TODO
 */
public class SalesPromotionSaveRequestVo {

    /**
     * 商家id
     */
    @NotNull(message = "店铺id不能为空")
    private Integer storeId;

    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Integer productId;

    /**
     * 标题
     */
    @NotEmpty(message = "活动标题不能为空")
    private String title;

    /**
     * 限购数量
     */
    @NotNull(message = "商品限购数量不能为空")
    private Integer purchasingAmount;

    /**
     * 折扣
     */
    @NotNull(message = "商品折扣不能为空")
    private BigDecimal discount;

    /**
     * 开始时间
     */
    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime deadline;

    @Override
    public String toString() {
        return "SalesPromotionSaveRequestVo{" +
                "storeId=" + storeId +
                ", productId=" + productId +
                ", title='" + title + '\'' +
                ",purchasingAmount="+purchasingAmount+
                ", discount=" + discount +
                ", startTime=" + startTime +
                ", deadline=" + deadline +
                '}';
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
