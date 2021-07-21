package com.stack.dogcat.gomall.user.requestVo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AdminEmailLoginRequestVo {

    @NotEmpty
    private String email;

    @NotEmpty
    private String verifyCode;

}
