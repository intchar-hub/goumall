package com.stack.dogcat.gomall.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.stack.dogcat.gomall.annotation.CurrentUser;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.user.responseVo.CustomerInfoResponseVo;
import com.stack.dogcat.gomall.user.service.ICustomerService;
import com.stack.dogcat.gomall.user.service.impl.TokenServiceImpl;
import com.stack.dogcat.gomall.utils.AppConst;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
            Customer customer = customerService.queryCustomerByOpenid(openId);
            //用户已经授权过信息
            if (customer!=null){
                //自定义方法用openId和session_key换取token(login_key)
                TokenServiceImpl tokenService =new TokenServiceImpl();
                String loginKey= tokenService.getToken(openId,session_key);
                Integer id = customer.getId();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id",id);
                map.put("loginKey",loginKey);
                SysResult result = SysResult.build(200,"登录成功",map);
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

    @PostMapping ("/saveCustomerInfo")
    @ResponseBody
    @Token.PassToken
    //用户授权
    public SysResult saveCustomerInfo(String userInfo,String code){

        String url ="https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", AppConst.App_id);
        requestMap.put("secret", AppConst.App_secret);
        requestMap.put("code", code);

        try{
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class,requestMap);
            JSONObject jsonObject=JSONObject.parseObject(responseEntity.getBody());
            String openId=jsonObject.getString("openid");
            String session_key=jsonObject.getString("session_key");

            JSONObject info = JSONObject.parseObject(userInfo);
            String userName = info.getString("nickName");
            String avatarPath = info.getString("avatarUrl");
            Integer gender = info.getIntValue("gender");

            customerService.insertCustomer(openId,session_key,userName,avatarPath,gender);
            SysResult result =SysResult.success();
            return result;
        }
        catch (Exception ex) {
            SysResult result = SysResult.error(ex.getMessage());
            return result;
        }
    }


    @GetMapping("/getCustomerInfo")
    @ResponseBody
    @Token.UserLoginToken
    //用户获取个人信息
    public SysResult getCustomerInfo(@CurrentUser Customer current_customer){
        try{
            CustomerInfoResponseVo responseVo = CopyUtil.copy(current_customer,CustomerInfoResponseVo.class);
            SysResult result = SysResult.success(responseVo);
            return result;
        }catch (Exception ex)
        {
            SysResult result =SysResult.error(ex.getMessage());
            return result;
        }
    }


    @PostMapping("/updateCustomerInfo")
    @ResponseBody
    @Token.UserLoginToken
    //用户修改个人信息
    public SysResult updateCustomerInfo(Integer id,String userName,Integer gender,String avatarPath,String phoneNumber,Integer age){
        try{
            Customer customer = null;
            customer.setId(id);
            customer.setUserName(userName);
            customer.setGender(gender);
            customer.setAvatorPath(avatarPath);
            customer.setPhoneNumber(phoneNumber);
            customer.setAge(age);
            customerService.updateCustomerInfoById(customer);
            SysResult result =SysResult.success();
            return result;
        }catch (Exception ex){
            SysResult result = SysResult.error(ex.getMessage());
            return result;
        }
    }



}
