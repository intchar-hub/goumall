package com.stack.dogcat.gomall.user.service.impl;

import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.user.service.IStoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Random;

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

    private String emailServiceCode;

    /**
     * 向商家发送邮箱验证码（四位数）
     * @param email
     */
    @Override
    public void sendEmailCode(String email) {
        emailServiceCode = String.format("%04d", new Random().nextInt(9999));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("注册验证码");
        message.setText("验证码：" + emailServiceCode);
        message.setFrom("1453370940@qq.com");
        message.setTo(email);
        mailSender.send(message);
    }
}
