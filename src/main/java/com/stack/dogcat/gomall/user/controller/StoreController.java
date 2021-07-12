package com.stack.dogcat.gomall.user.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.user.requestVo.StoreRegisterRequestVo;
import com.stack.dogcat.gomall.user.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * <p>
 * 商店信息表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@CrossOrigin
@RestController
@RequestMapping("/user/store")
public class StoreController {

    @Autowired
    IStoreService storeService;

    /**
     * 商家获取图片验证码
     * @param request
     * @param response
     */
    @GetMapping("/getStringCode")
    public void getStringCode(HttpServletRequest request, HttpServletResponse response, String userName) {
        try {
            storeService.getStringCode(request, response, userName);
        }catch (Exception e){
            e.printStackTrace();
//            return SysResult.error("验证码生成失败");
        }
//        return SysResult.success();
    }

    /**
     * 商家获取邮箱验证码
     * @param email
     * @return
     */
    @GetMapping("/getEmailCode")
    public SysResult getEmailCode(HttpServletRequest request, String email) {
        try {
            storeService.sendEmailCode(request, email);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return SysResult.error("验证码发送失败");
        }
        return SysResult.success();
    }

    /**
     * 商家注册
     * @param registerRequestVo
     * @return
     */
    @PostMapping("/register")
    public SysResult register(HttpServletRequest request, @Valid @RequestBody StoreRegisterRequestVo registerRequestVo) {
        try {
            storeService.register(request, registerRequestVo);
        } catch (Exception e) {
            System.out.println(e);
            return SysResult.error("注册失败");
        }
        return SysResult.success();
    }

}
