package com.stack.dogcat.gomall.sales.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xrm
 * @since 2021-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sms_user_coupon")
public class UserCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private Integer couponId;

    /**
     * 0-可使用（未使用且未失效），1-已使用
     */
    private Integer status;


}
