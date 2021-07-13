package com.stack.dogcat.gomall.user.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl  {

    public String getToken(String openid,String session_key) {
        String token="";
        token= JWT.create().withAudience(openid)
                .sign(Algorithm.HMAC256(session_key));
        return token;
    }
}


