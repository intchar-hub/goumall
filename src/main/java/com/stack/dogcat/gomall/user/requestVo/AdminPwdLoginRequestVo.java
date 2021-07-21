package com.stack.dogcat.gomall.user.requestVo;

import lombok.Data;

@Data
public class AdminPwdLoginRequestVo {

    private String userName;

    private String password;

    private String verifyString;

}
