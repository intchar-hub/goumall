package com.stack.dogcat.gomall.product.responseVo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductWithCommentResponseVo {

    private Integer id;

    /**
     * 商品名
     */
    private String name;

    /**
     * 添加商品时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 商品展示图片路径
     */
    private String imagePath;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品的评论量
     */
    private Integer CommentNum;

    /**
     * 商品库存
     */
    private Integer stockNum;

}
