package com.stack.dogcat.gomall.message.requestVo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CommentSaveRequestVo {

    /**
     * 顾客id
     */
    @NotNull(message = "顾客id不能为空")
    private Integer customerId;

    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Integer productId;

    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Integer orderId;

    /**
     * 评分星级,1-5
     */
    @NotNull(message = "评分星级不能为空")
    private Integer level;

    /**
     * 商品评论
     */
    @NotEmpty(message = "评论内容不能为空")
    private String content;

}
