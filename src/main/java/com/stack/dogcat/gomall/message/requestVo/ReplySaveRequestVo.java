package com.stack.dogcat.gomall.message.requestVo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ReplySaveRequestVo {

    /**
     * 评论id
     */
    @NotNull(message = "评论id不能为空")
    private Integer commentId;

    /**
     * 回复内容
     */
    @NotEmpty(message = "评论内容不能为空")
    private String content;

}
