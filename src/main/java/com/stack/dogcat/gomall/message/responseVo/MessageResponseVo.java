package com.stack.dogcat.gomall.message.responseVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseVo implements Comparable<MessageResponseVo> {

    private String receiverName;

    private String receiverAvatarPath;

    private String message;

    private LocalDateTime gmtCreate;

    private Integer senderType;

    @Override
    public int compareTo(MessageResponseVo o) {
        return this.gmtCreate.compareTo(o.getGmtCreate());
    }
}
