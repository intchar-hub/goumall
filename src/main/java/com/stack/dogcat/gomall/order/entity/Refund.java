package com.stack.dogcat.gomall.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 退款表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oms_refund")
public class Refund implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 消费者id
     */
    private Integer customerId;

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 退款状态，0->未退款；1->已退款；2->商家不同意退款；3->消费者已取消
     */
    private Integer status;

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
