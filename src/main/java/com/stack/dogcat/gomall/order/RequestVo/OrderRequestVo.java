package com.stack.dogcat.gomall.order.RequestVo;

import lombok.Data;

@Data
public class OrderRequestVo {

    private Integer storeId;

    private Integer productId;

    private String productAttribute;

    private Integer productNum;

    private Integer receiveAddressId;

    private Integer couponId;
}
