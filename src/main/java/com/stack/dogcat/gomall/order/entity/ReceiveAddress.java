package com.stack.dogcat.gomall.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 收货地址表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oms_receive_address")
public class ReceiveAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收货地址id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer customerId;

    /**
     * 收件人
     */
    private String consignee;

    /**
     * 地址
     */
    private String address;

    /**
     * 收货人电话
     */
    private String phoneNumber;

    /**
     * 默认地址，0->不是默认地址，1->是默认地址
     */
    private Integer defaultAddress;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;


}
