package com.stack.dogcat.gomall.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oms_cart_item")
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车项id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer customerId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品SKUid
     */
    private Integer skuId;

    /**
     * 待下单商品数
     */
    private Integer productNum;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;


}
