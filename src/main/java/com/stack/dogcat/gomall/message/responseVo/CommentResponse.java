package com.stack.dogcat.gomall.message.responseVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private Integer commentId;

    private String customerName;

    private String avatarPath;

    private String content;

    private Integer level;

    private LocalDateTime gmtCreate;

}
