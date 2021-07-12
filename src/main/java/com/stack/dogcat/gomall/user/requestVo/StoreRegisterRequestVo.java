package com.stack.dogcat.gomall.user.requestVo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Author Yang Jie
 * @Date 2021/7/12 14:32
 * @Descrition TODO
 */
public class StoreRegisterRequestVo {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "店铺名不能为空")
    private String storeName;

    @NotEmpty(message = "邮箱不能为空")
    @Pattern(regexp = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$", message = "邮箱格式错误")
    private String email;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "验证码不能为空")
    private String verifyCode;

    @Override
    public String toString() {
        return "StoreRegisterRequestVo{" +
                "userName='" + userName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
