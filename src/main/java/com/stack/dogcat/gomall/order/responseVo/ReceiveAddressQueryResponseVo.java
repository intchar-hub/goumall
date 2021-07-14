package com.stack.dogcat.gomall.order.responseVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ReceiveAddressQueryResponseVo {
    /**
     * 收货地址id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer customerId;

    /**
     * 地址
     */
    private String address;

    /**
     * 默认地址，0->不是默认地址，1->是默认地址
     */
    private Integer defaultAddress;
}
