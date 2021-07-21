package com.stack.dogcat.gomall.user.requestVo;

import lombok.Data;

@Data
public class AdminEmailLoginRequestVo {

    private String email;

    private String verifyCode;

}
