package com.stack.dogcat.gomall.message.responseVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseVo {

    private CommentResponse customer;

    private ReplyResponse store;

}
