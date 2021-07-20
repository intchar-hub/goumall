package com.stack.dogcat.gomall.message.responseVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoreChatListResponseVo {

    private Integer chatUserLinkId;

    private String customerName;

    private String customerAvatorPath;

    private String latestMessage;

    private LocalDateTime latestGmtCreate;

    private Integer unreadNum;

}
