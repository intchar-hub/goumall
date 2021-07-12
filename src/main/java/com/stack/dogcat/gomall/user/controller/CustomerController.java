package com.stack.dogcat.gomall.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.user.service.ICustomerService;
import com.stack.dogcat.gomall.user.service.ITokenService;
import com.stack.dogcat.gomall.user.service.impl.TokenServiceImpl;
import com.stack.dogcat.gomall.utils.AppConst;
import com.stack.dogcat.gomall.utils.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/user/customer")
public class CustomerController {
    @Autowired
    ICustomerService customerService;
    @Autowired
    ITokenService tokenService;

    @Resource
    private RestTemplate restTemplate;


    @PostMapping ("/customerLogin")
    @ResponseBody
    @Token.PassToken
    //用户自动登录
    public SysResult customerLogin(String code){
        String url ="https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", AppConst.App_id);
        requestMap.put("secret", AppConst.App_secret);
        requestMap.put("code", code);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class,requestMap);
        JSONObject jsonObject=JSONObject.parseObject(responseEntity.getBody());
        String openId=jsonObject.getString("openid");
        String session_key=jsonObject.getString("session_key");

        try{
            //用户已经授权过信息
            if (customerService.queryCustomerByOpenid(openId)!=null){
                //自定义方法用openId和session_key换取token(login_key)
                String loginKey= tokenService.getToken(openId,session_key);
                SysResult result = SysResult.build(200,"登录成功",loginKey);
                return result;
            }
            else{
                //用户未授权过
                SysResult result = SysResult.build(400,"未查询到用户授权信息",null);
                return result;
            }
        }catch (Exception ex){
            //出现异常  status:-1
            String msg = "检索用户出现异常，具体异常:" + ex.getMessage();
            SysResult result = SysResult.build(-1,msg,null);
            return result;
        }
    }



}
