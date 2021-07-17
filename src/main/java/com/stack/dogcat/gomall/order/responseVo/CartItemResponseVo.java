package com.stack.dogcat.gomall.order.responseVo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartItemResponseVo {

    /**
     * 用户id
     */
    private Integer customerId;


    /**
     * 购物车项id
     */
    private Integer cartItemId;

    /**
     * 商店id
     */
    private Integer storeId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品SKUid
     */
    private Integer skuId;

    /**
     * 商店名
     */
    private String storeName;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品图片路径
     */
    private String imagePath;

    /**
     * Sku规格
     */
    private String productAttribute;

    /**
     * Sku价格
     */
    private BigDecimal price;

    /**
     * 待下单商品数
     */
    private Integer productNum;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;
}
