package com.stack.dogcat.gomall.user.requestVo;

/**
 * @Author Yang Jie
 * @Date 2021/7/21 8:23
 * @Descrition TODO
 */
public class StoreEmailLoginRequestVo {

    private String email;
    private String verifyCode;

    @Override
    public String toString() {
        return "StoreEmailLoginRequestVo{" +
                "email='" + email + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
