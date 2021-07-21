package com.stack.dogcat.gomall.user.requestVo;

import javax.validation.constraints.NotEmpty;

/**
 * @Author Yang Jie
 * @Date 2021/7/21 9:23
 * @Descrition TODO
 */
public class StorePwdLoginRequestVo {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "验证码不能为空")
    private String verifyCode;

    @NotEmpty(message = "验证码标识不能为空")
    private String markString;

    @Override
    public String toString() {
        return "StorePwdLoginRequestVo{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", markString='" + markString + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getMarkString() {
        return markString;
    }

    public void setMarkString(String markString) {
        this.markString = markString;
    }
}
