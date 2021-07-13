package com.stack.dogcat.gomall.user.responseVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminLoginResponseVo {

    private Integer id;

    private String username;

    private String email;

    private String phoneNumber;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

}
