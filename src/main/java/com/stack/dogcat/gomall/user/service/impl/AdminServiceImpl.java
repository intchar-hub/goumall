package com.stack.dogcat.gomall.user.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.entity.Admin;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.AdminMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.user.requestVo.AdminEmailLoginRequestVo;
import com.stack.dogcat.gomall.user.requestVo.AdminPwdLoginRequestVo;
import com.stack.dogcat.gomall.user.responseVo.AdminLoginResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.user.service.IAdminService;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 管理员信息表 服务实现类
 * </p>
 *
 * @author xrm
 * @update kxy
 * @since 2021-07-08
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    JavaMailSenderImpl mailSender;



    /**
     * 管理员查看所有商家信息
     */
    @Override
    public PageResponseVo<StoreInfoResponseVo> listStoreInfo(Integer pageNum, Integer pageSize){

        Page<Store> page = new Page<>(pageNum,pageSize);
        IPage<Store> storePage = storeMapper.selectPage(page,null);
        //封装vo
        PageResponseVo<StoreInfoResponseVo> storePageResponseVo = new PageResponseVo(storePage);
        storePageResponseVo.setData(CopyUtil.copyList(storePageResponseVo.getData(), StoreInfoResponseVo.class));
        return storePageResponseVo;

    }

    /**
     * 管理员审核商家注册
     */
    @Override
    public Integer examineStoreRegister(Integer id,Integer flag){
        //审核不通过
        if(flag==0){
            UpdateWrapper<Store> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id).set("status",2);
            //发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("审核结果");
            message.setText("抱歉，您的审核未通过");
            message.setFrom("w741008w@163.com");
            message.setTo(storeMapper.selectById(id).getEmail());
            mailSender.send(message);
            return storeMapper.update(null,updateWrapper);
        }
        //审核通过
        else {
            UpdateWrapper<Store> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id).set("status",1);
            //发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("审核结果");
            message.setText("恭喜，您的审核已通过");
            message.setFrom("w741008w@163.com");
            message.setTo(storeMapper.selectById(id).getEmail());
            mailSender.send(message);
            return storeMapper.update(null,updateWrapper);
        }
    }

    /**
     * 向管理员发送邮箱验证码（四位数）
     * @param email
     */
    @Override
    public Integer sendEmailCode(HttpServletRequest request, String email) {
        String adminString = adminMapper.selectById(1).getEmail();
        if(!email.equals(adminString)){
            System.out.println(email);
            System.out.println();
            System.out.println(adminString);
            return 0;
        }
        else{
            String emailServiceCode = String.format("%04d", new Random().nextInt(9999));
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("注册验证码");
            message.setText("验证码（五分钟内有效）：" + emailServiceCode);
            message.setFrom("w741008w@163.com");
            message.setTo(email);
            mailSender.send(message);

            //字符验证码放入session
            request.getSession().setAttribute("admin", emailServiceCode);
            //定时5分钟清除该session
            removeAttrbute(request.getSession(), "admin");

            return 1;
        }
    }


    /**
     * 管理员获取字符验证码
     * @param request
     * @param response
     */
    @Override
    public void getStringCode(HttpServletRequest request, HttpServletResponse response) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(90, 40);

        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
//        return lineCaptcha.getImageBase64();
        try {
            lineCaptcha.write(response.getOutputStream());

            //字符验证码放入session
            request.getSession().setAttribute("admin", lineCaptcha.getCode());
            //定时5分钟清除该session
            removeAttrbute(request.getSession(), "admin");
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

    /**
     * 管理员邮箱登录
     * @param request
     * @return
     */
    @Override
    public AdminLoginResponseVo emailLogin(HttpServletRequest request, AdminEmailLoginRequestVo adminEmailLoginRequestVo) {

        String email=adminEmailLoginRequestVo.getEmail();
        String verifyCode=adminEmailLoginRequestVo.getVerifyCode();
        //判断邮箱验证码是否正确
        HttpSession session = request.getSession();
        String correctCode = (String)session.getAttribute("admin");
        LOG.info("管理员" + email + "验证码：" + correctCode);
        if(correctCode == null || correctCode.isEmpty()) {
            throw new RuntimeException("验证码已失效或邮箱错误");
        }
        if(!correctCode.equals(verifyCode)) {
            throw new RuntimeException("验证码错误");
        }

        Admin adminDB = adminMapper.selectOne(new QueryWrapper<Admin>().eq("email",email));

        if(adminDB != null) {
            AdminLoginResponseVo responseVo = CopyUtil.copy(adminDB, AdminLoginResponseVo.class);
            return responseVo;
        } else {
            throw new RuntimeException("邮箱错误");
        }
    }

    /**
     * 管理员密码登录
     * @return
     */
    @Override
    public AdminLoginResponseVo pwdLogin(HttpServletRequest request, AdminPwdLoginRequestVo adminPwdLoginRequestVo) {

        String userName = adminPwdLoginRequestVo.getUserName();
        String password = adminPwdLoginRequestVo.getPassword();
        String verifyString = adminPwdLoginRequestVo.getVerifyString();
        //判断字符验证码是否正确
        HttpSession session = request.getSession();
        String correctCode = (String)session.getAttribute("admin");
        LOG.info("管理员" + userName + "验证码：" + correctCode);
        if(correctCode == null || correctCode.isEmpty()) {
            throw new RuntimeException("验证码已失效");
        }
        if(!correctCode.equals(verifyString)) {
            throw new RuntimeException("验证码错误");
        }

        Admin adminDB = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",userName));

        if(adminDB != null && adminDB.getPassword().equals(password)) {
            AdminLoginResponseVo responseVo = CopyUtil.copy(adminDB, AdminLoginResponseVo.class);
            return responseVo;
        } else {
            throw new RuntimeException("用户名或密码错误");
        }
    }

}
