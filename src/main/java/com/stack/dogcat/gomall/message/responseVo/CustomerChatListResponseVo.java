package com.stack.dogcat.gomall.message.responseVo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerChatListResponseVo {

    private Integer chatUserLinkId;

    private String storeName;

    private String storeAvatorPath;

    private String latestMessage;

    private LocalDateTime latestGmtCreate;

    private Integer unreadNum;
}
