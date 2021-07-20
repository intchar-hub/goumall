package com.stack.dogcat.gomall.message.requestVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageSaveRequestVo {

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

}
