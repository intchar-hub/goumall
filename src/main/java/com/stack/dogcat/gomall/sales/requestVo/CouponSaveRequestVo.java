package com.stack.dogcat.gomall.sales.requestVo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class CouponSaveRequestVo {

    /**
     * 商家id
     */
    @NotNull(message = "店铺id不能为空")
   private Integer storeId;

    /**
     * 可以使用的价格
     */
    @NotNull(message = "可以使用的价格不能为空")
    private BigDecimal targetPrice;

    /**
     * 满减金额
     */
    @NotNull(message = "满减金额不能为空")
    private BigDecimal discount;

    /**
     * 优惠券开始生效时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空")
    private LocalDateTime deadline;

}
