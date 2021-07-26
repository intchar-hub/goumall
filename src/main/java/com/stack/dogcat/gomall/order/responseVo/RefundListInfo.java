package com.stack.dogcat.gomall.order.responseVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefundListInfo {

    /**
     * 退款id
     */
    private Integer refundId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品数
     */
    private Integer productNum;

    /**
     * 商品图片路径
     */
    private String productAvatarPath;

    /**
     * 退款状态，0->未退款（已申请）；1->已退款；2->商家不同意退款；3->消费者已取消
     */
    private Integer refundStatus;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;


}
