package com.stack.dogcat.gomall.message.responseVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyResponse {

    private String content;

    private LocalDateTime gmtCreate;

}
