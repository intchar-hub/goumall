package com.stack.dogcat.gomall.user.responseVo;

import lombok.Data;

@Data
public class StoreInfoResponseVo {

    private Integer id;

    private String userName;

    private String avatarPath;

    private String email;

    private String storeName;

    /**
     * 发货地址
     */
    private String shipAddress;

    private Integer fansNum;

    /**
     * 0是待审核，1是审核通过，2是审核不通过，3是已封禁
     */
    private Integer status;


}
