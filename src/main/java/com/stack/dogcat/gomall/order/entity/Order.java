package com.stack.dogcat.gomall.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oms_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商家id
     */
    private Integer storeId;

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
     * 订单编号
     */
    private String orderNumber;

    /**
     * 商品数
     */
    private Integer productNum;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 订单状态，0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭
     */
    private Integer status;

    /**
     * 收货人电话
     */
    private String phoneNumber;

    /**
     * 收货地址
     */
    private String address;


    /**
     * 优惠券id
     */
    private Integer couponId;

    /**
     * 优惠券金额
     */
    private BigDecimal couponDiscount;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 商家是否可见0->不可见 1->可见
     */
    private Integer storeVisible;

    /**
     * 消费者是否可见0->不可见 1->可见
     */
    private Integer customerVisible;

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
