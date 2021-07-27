package com.stack.dogcat.gomall.order.responseVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RefundOrderInfo {

    /**
     * 退款id
     */
    private Integer refundId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 用户id
     */
    private Integer customerId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品展示图片路径
     */
    private String imagePath;

    /**
     * 商品SKUid
     */
    private Integer skuId;

    /**
     * 规格属性集合，json表示
     */
    private String productAttribute;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 商品数
     */
    private Integer productNum;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 订单状态，0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；
     */
    private Integer status;

    /**
     * 退款状态，0->无退款申请；1->退款申请中；2->已退款；3->退款取消；4->退款失败
     */
    private Integer refundStatus;

    /**
     * 收货地址id
     */
    private Integer receiveAddressId;

    /**
     * 收件人
     */
    private String consignee;

    /**
     * 收货人电话
     */
    private String phoneNumber;

    /**
     * 收货地址
     */
    private String address;


    /**
     * 优惠券id
     */
    private Integer couponId;

    /**
     * 优惠券金额
     */
    private BigDecimal couponDiscount;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 退款创建时间
     */
    private LocalDateTime refundCreate;

    /**
     * 订单创建时间
     */
    private LocalDateTime orderCreate;
}
