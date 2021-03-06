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
import java.time.LocalDateTime;
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
@CrossOrigin
@RequestMapping("/user/customer")
public class CustomerController {
    @Autowired
    ICustomerService customerService;


    @Resource
    private RestTemplate restTemplate;


    /**
     * 用户自动登录
     * @param code
     * @return
     */
    @PostMapping ("/customerLogin")
    @ResponseBody
    @Token.PassToken
    public SysResult customerLogin(String code){
        String url ="https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", AppConst.App_id);
        requestMap.put("secret", AppConst.App_secret);
        requestMap.put("js_code", code);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class,requestMap);
        JSONObject jsonObject=JSONObject.parseObject(responseEntity.getBody());
        String openId=jsonObject.getString("openid");
        String session_key=jsonObject.getString("session_key");

        try{
            Customer customer = customerService.queryCustomerByOpenid(openId);
            Integer id;
            //自定义方法用openId和session_key换取token(login_key)
            TokenServiceImpl tokenService = new TokenServiceImpl();
            String loginKey;
            if (customer != null) {
                //用户已经授权过信息
                id = customer.getId();
                loginKey = customer.getLoginKey();
            }else{
                //用户未授权过
                loginKey = tokenService.getToken(openId, session_key);
                customerService.insertCustomer(openId,session_key,"","",0);
                id =customerService.queryCustomerByOpenid(openId).getId();
                Customer customer1=customerService.queryCustomerByCustomerId(id);
                customer1.setLoginKey(loginKey);
                customerService.updateCustomerInfoById(customer1);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("token", loginKey);
            SysResult result = SysResult.build(200, "登录成功", map);
            return result;


        }catch (Exception ex){
            //出现异常  status:-1
            String msg = "检索用户出现异常，具体异常:" + ex.getMessage();
            SysResult result = SysResult.build(-1,msg,null);
            return result;
        }
    }


    /**
     * 用户授权
     * @param userInfo
     * @param current_customer
     * @return
     */
    @PostMapping ("/saveCustomerInfo")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult saveCustomerInfo(@CurrentUser Customer current_customer,String userInfo){

        try{
            JSONObject info = JSONObject.parseObject(userInfo);
            String userName = info.getString("nickName");
            String avatarPath = info.getString("avatarUrl");
            Integer gender = info.getIntValue("gender");

            current_customer.setUserName(userName);
            current_customer.setAvatorPath(avatarPath);
            current_customer.setGender(gender);
            current_customer.setGmtCreate(LocalDateTime.now());
            customerService.updateCustomerInfoById(current_customer);

            SysResult result =SysResult.success();
            return result;
        }
        catch (Exception ex) {
            SysResult result = SysResult.error(ex.getMessage());
            return result;
        }
    }



    /**
     * 用户获取个人信息
     * @param current_customer
     * @return
     */
    @GetMapping("/getCustomerInfo")
    @ResponseBody
    @Token.UserLoginToken
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



    /**
     * 用户修改个人信息
     * @param id,userName,gender,avatarPath,phoneNumber,age
     * @return
     */
    @PostMapping("/updateCustomerInfo")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult updateCustomerInfo(Integer id,String userName,Integer gender,String avatarPath,String phoneNumber,Integer age){
        try{
            Customer customer = new Customer();
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


    @GetMapping("/testToken")
    @ResponseBody
    @Token.PassToken
    public String testToken(){
        TokenServiceImpl tokenService =new TokenServiceImpl();
        String token = tokenService.getToken("oqHom5T64SZbL22Uj0dQzv_UxyXQ","K1wVmMG6duGulERS7SWq/A==");
        return token;
    }

}
