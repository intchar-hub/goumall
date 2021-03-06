package com.stack.dogcat.gomall.order.responseVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderInfoResponseVo {

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 商品id
     */
    private Integer productId;

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
     * 收件人
     */
    private String consignee;

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
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 订单状态，0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；
     */
    private Integer status;

    /**
     * 评分星级,1-5
     */
    private Integer level;

    /**
     * 商品评论
     */
    private String content;

    /**
     * 退款状态，0->无退款申请；1->退款申请中；2->已退款；3->退款取消；4->退款失败
     */
    private Integer refundStatus;

    /**
     * 订单创建时间
     */
    private LocalDateTime orderGmtCreate;


}
