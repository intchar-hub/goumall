package com.stack.dogcat.gomall.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("mms_chat_message")
public class ChatMessage implements Serializable {

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
     * 消息内容
     */
    private String content;

    /**
     * 消息创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 是否是最新一条消息
     */
    private Integer latest;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;


}
