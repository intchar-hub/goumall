package com.stack.dogcat.gomall.content.responseVo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCollectionResponseVo {

    private Integer productCollectionId;

    private Integer productId;

    private String productName;

    /**
     * 商品最高价格（规格不同，价格不同）
     */
    private BigDecimal highestPrice;


    /**
     * 商品最低价格（规格不同，价格不同）
     */
    private BigDecimal lowestPrice;

    /**
     * 商品展示图片路径
     */
    private String imagePath;

    /**
     * 商品描述
     */
    private String description;
}
