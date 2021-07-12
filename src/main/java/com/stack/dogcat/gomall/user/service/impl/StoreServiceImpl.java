package com.stack.dogcat.gomall.user.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.user.requestVo.StoreRegisterRequestVo;
import com.stack.dogcat.gomall.user.service.IStoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * 商店信息表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements IStoreService {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    StoreMapper storeMapper;

    /**
     * 向商家发送邮箱验证码（四位数）
     * @param email
     */
    @Override
    public void sendEmailCode(HttpServletRequest request, String email) {
        String emailServiceCode = String.format("%04d", new Random().nextInt(9999));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("注册验证码");
        message.setText("验证码（五分钟内有效）：" + emailServiceCode);
        message.setFrom("w741008w@163.com");
        message.setTo(email);
        mailSender.send(message);

        //字符验证码放入session
        request.getSession().setAttribute("store-" + email, emailServiceCode);
        //定时5分钟清除该session
        removeAttrbute(request.getSession(), "store-" + email);
    }

    /**
     * 商家注册
     * @param registerRequestVo
     */
    @Override
    public void register(HttpServletRequest request, StoreRegisterRequestVo registerRequestVo) {

        //判断字符验证码是否正确
        HttpSession session = request.getSession();
        String correctCode = (String)session.getAttribute("store-" + registerRequestVo.getEmail());
        System.out.println("correctCode: " + correctCode);
        if(correctCode == null || correctCode.isEmpty()) {
            throw new RuntimeException("验证码已失效");
        }
        if(!correctCode.equals(registerRequestVo.getVerifyCode())) {
            throw new RuntimeException("验证码错误");
        }

        Store store = new Store();
        store.setUserName(registerRequestVo.getUserName());
        store.setStoreName(registerRequestVo.getStoreName());
        store.setPassword(registerRequestVo.getPassword());
        store.setEmail(registerRequestVo.getEmail());
        storeMapper.insert(store);
    }

    /**
     * 商家获取字符验证码
     * @param request
     * @param response
     * @param userName
     */
    @Override
    public void getStringCode(HttpServletRequest request, HttpServletResponse response, String userName) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(90, 40);

        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
//        return lineCaptcha.getImageBase64();
        try {
            lineCaptcha.write(response.getOutputStream());

            //字符验证码放入session
            request.getSession().setAttribute("store-" + userName, lineCaptcha.getCode());
            //定时5分钟清除该session
            removeAttrbute(request.getSession(), "store-" + userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置5分钟后删除session中的验证码
     * @param session
     * @param attrName
     */
    private void removeAttrbute(final HttpSession session, final String attrName) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 删除session中存的验证码
                session.removeAttribute(attrName);
                timer.cancel();
            }
        }, 60 * 1000);
    }

}
