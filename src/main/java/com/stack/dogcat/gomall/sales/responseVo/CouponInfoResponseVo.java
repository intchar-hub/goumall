package com.stack.dogcat.gomall.sales.responseVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponInfoResponseVo {

    /**
     * 优惠券id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 优惠券开始生效时间
     */
    private LocalDateTime startTime;

    /**
     * 过期时间
     */
    private LocalDateTime deadline;

}
