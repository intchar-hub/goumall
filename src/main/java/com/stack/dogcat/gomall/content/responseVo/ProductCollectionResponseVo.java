package com.stack.dogcat.gomall.content.responseVo;

import lombok.Data;

@Data
public class ProductCollectionResponseVo {

    private Integer productCollectionId;

    private Integer productId;

    private String productName;

    /**
     * 商品展示图片路径
     */
    private String imagePath;

    /**
     * 商品描述
     */
    private String description;
}
