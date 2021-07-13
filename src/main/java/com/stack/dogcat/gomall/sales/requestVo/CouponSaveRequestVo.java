package com.stack.dogcat.gomall.sales.requestVo;

import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class CouponSaveRequestVo {

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 可以使用的价格
     */
    private BigDecimal targetPrice;

    /**
     * 满减金额
     */
    private BigDecimal discount;

    /**
     * 优惠券开始生效时间
     */
    private LocalDateTime startTime;

    /**
     * 过期时间
     */
    private LocalDateTime deadline;

}
