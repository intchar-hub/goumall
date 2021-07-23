package com.stack.dogcat.gomall.order.responseVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefundResponseVo {

    /**
     * 退款id
     */
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 退款状态，0->未退款；1->已退款；2->商家不同意退款；3->消费者已取消
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;
}
