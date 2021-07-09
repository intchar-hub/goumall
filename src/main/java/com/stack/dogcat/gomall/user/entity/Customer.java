package com.stack.dogcat.gomall.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String openId;

    private String sessionKey;

    private String userName;

    private String phoneNumber;

    private Integer age;

    /**
     * 0男 1女
     */
    private Integer gender;

    private String avatorPath;

    private String loginKey;

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
