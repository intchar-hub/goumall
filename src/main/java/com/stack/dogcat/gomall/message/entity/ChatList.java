package com.stack.dogcat.gomall.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mms_chat_list")
public class ChatList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 私信会话id
     */
    private Integer chatUserLinkId;

    /**
     * 发送方，0是顾客，1是商家
     */
    private Integer senderType;

    /**
     * 未读消息数
     */
    private Integer unreadNum;

    /**
     * 顾客是否在窗口（在线）
     */
    private Integer customerWindow;

    /**
     * 商家是否在窗口（在线）
     */
    private Integer storeWindow;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;


}
