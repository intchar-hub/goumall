package com.stack.dogcat.gomall.content.responseVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintResponseVo {

    /**
     * 投诉id
     */
    private Integer id;

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 商家名
     */
    private String storeName;

    /**
     * 用户id
     */
    private Integer customerId;

    /**
     * 用户名
     */
    private String customerName;

    /**
     * 投诉内容
     */
    private String content;

    /**
     * 投诉状态，0表示未受理，1表示已受理
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

}
