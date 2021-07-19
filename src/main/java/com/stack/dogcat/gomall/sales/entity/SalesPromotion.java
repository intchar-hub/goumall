package com.stack.dogcat.gomall.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 活动表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sms_sales_promotion")
public class SalesPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 标题
     */
    private String title;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime deadline;

    /**
     * 0->正常 1->过期
     */
    private Integer status;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;


}
