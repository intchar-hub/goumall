package com.stack.dogcat.gomall.user.requestVo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AdminPwdLoginRequestVo {

    @NotEmpty
    private String userName;

    @NotEmpty
    private String password;

    @NotEmpty
    private String verifyCode;

}
