package com.stack.dogcat.gomall.message.responseVo;

import com.stack.dogcat.gomall.message.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentQueryByStoreResponseVo {

    private Integer productId;

    private String productName;

    private String imagePath;

    private LocalDateTime gmtCreate;

    private List<CommentResponseVo> comments;

}
