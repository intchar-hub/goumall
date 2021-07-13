package com.stack.dogcat.gomall.user.responseVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerInfoResponseVo {

    private Integer id;

    private String openId;

    private String sessionKey;

    private String userName;

    private String phoneNumber;

    private Integer age;

    /**
     * 0男 1女
     */
    private Integer gender;

    private String avatorPath;

    private String loginKey;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

}
