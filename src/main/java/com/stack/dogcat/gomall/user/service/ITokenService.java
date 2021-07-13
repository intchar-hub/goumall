package com.stack.dogcat.gomall.user.service;

import com.stack.dogcat.gomall.user.entity.Customer;

public interface ITokenService {
    //获取登录凭证
    String getToken(String openid,String session_key);
}
