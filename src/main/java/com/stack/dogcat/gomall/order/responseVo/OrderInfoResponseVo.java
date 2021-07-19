package com.stack.dogcat.gomall.order.responseVo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderInfoResponseVo {

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 商店名
     */
    private String storeName;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品展示图片路径
     */
    private String productImagePath;

    /**
     * 规格属性集合，json表示
     */
    private String productAttribute;

    /**
     * 该规格下的商品价格
     */
    private BigDecimal price;

    /**
     * 商品数
     */
    private Integer productNum;

    /**
     * 收货人电话
     */
    private String phoneNumber;

    /**
     * 地址
     */
    private String receiveAddress;

    /**
     * 优惠券金额
     */
    private BigDecimal discount;

    /**
     * 订单状态，0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    private LocalDateTime orderGmtCreate;


}