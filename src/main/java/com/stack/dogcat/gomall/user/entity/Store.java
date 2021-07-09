package com.stack.dogcat.gomall.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商店信息表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_store")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userName;

    private String password;

    private String email;

    private String storeName;

    private String phoneNumber;

    /**
     * 发货地址
     */
    private String shipAddress;

    private Integer fansNum;

    /**
     * 0是待审核，1是审核通过，2是审核不通过，3是已封禁
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
