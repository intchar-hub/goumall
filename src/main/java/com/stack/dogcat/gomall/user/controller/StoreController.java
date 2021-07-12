package com.stack.dogcat.gomall.user.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.stack.dogcat.gomall.user.service.IStoreService;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    LineCaptcha lineCaptcha;

    /**
     * 商家获取图片验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/getStringCode")
    public SysResult getStringCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            lineCaptcha = CaptchaUtil.createLineCaptcha(90, 40);
            request.getSession().setAttribute("CAPTCHA_KEY", lineCaptcha.getCode());
            response.setContentType("image/png");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            lineCaptcha.write(response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.error("验证码生成失败");
        }
        return SysResult.success();
    }

    /**
     * 商家获取邮箱验证码
     * @param email
     * @return
     */
    @GetMapping("/getEmailCode")
    public SysResult getEmailCode(String email) {
        try {
            storeService.sendEmailCode(email);
        } catch (Exception e) {
            System.out.println(e);
            return SysResult.error("验证码发送失败");
        }
        return SysResult.success();
    }

}
