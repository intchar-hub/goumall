package com.stack.dogcat.gomall.user.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.entity.Complaint;
import com.stack.dogcat.gomall.content.mapper.ComplaintMapper;
import com.stack.dogcat.gomall.user.entity.Admin;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.AdminMapper;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.user.requestVo.StoreRegisterRequestVo;
import com.stack.dogcat.gomall.user.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.responseVo.ComplaintResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
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

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    JavaMailSenderImpl mailSender;



    /**
     * 管理员查看所有商家信息
     */
    @Override
    public PageResponseVo<StoreInfoResponseVo> listStoreInfo(int pageNum, int pageSize){

        Page<Store> page = new Page<>(pageNum,pageSize);
        IPage<Store> storePage = storeMapper.selectPage(page,null);
        //封装vo
        PageResponseVo<StoreInfoResponseVo> storePageResponseVo = new PageResponseVo(storePage);
        storePageResponseVo.setData(CopyUtil.copyList(storePageResponseVo.getData(), StoreInfoResponseVo.class));
        return storePageResponseVo;

    }

    /**
     * 管理员查看所有投诉
     */
    @Override
    public PageResponseVo<ComplaintResponseVo> listComplaints(int pageNum, int pageSize){

        Page<Complaint> page = new Page<>(pageNum,pageSize);
        IPage<Complaint> complaintPage = complaintMapper.selectPage(page,null);
        //封装vo
        PageResponseVo<ComplaintResponseVo> complaintPageResponseVo = new PageResponseVo(complaintPage);
        List<ComplaintResponseVo> complaintVos=CopyUtil.copyList(complaintPageResponseVo.getData(), ComplaintResponseVo.class);

        //获取用户名和店铺名
        for (ComplaintResponseVo complaintVo:complaintVos) {
            complaintVo.setCustomerName(customerMapper.selectById(complaintVo.getCustomerId()).getUserName());
            complaintVo.setStoreName(storeMapper.selectById(complaintVo.getStoreId()).getStoreName());
        }
        complaintPageResponseVo.setData(complaintVos);
        return complaintPageResponseVo;

    }

    /**
     * 管理员处理投诉
     */
    @Override
    public int solveComplaints(int complaintId,int banned){
        if(banned==0){
            return 1;
        }
        else {
            int storeId=complaintMapper.selectById(complaintId).getStoreId();
            UpdateWrapper<Store> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",storeId).set("status",3);
            return storeMapper.update(null,updateWrapper);
        }
    }

    /**
     * 管理员审核商家注册
     */
    @Override
    public int examineStoreRegister(int id,int flag){
        //审核不通过
        if(flag==0){
            UpdateWrapper<Store> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id).set("status",2);
            return storeMapper.update(null,updateWrapper);
        }
        //审核通过
        else {
            UpdateWrapper<Store> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id).set("status",1);
            return storeMapper.update(null,updateWrapper);
        }
    }

    /**
     * 向管理员发送邮箱验证码（四位数）
     * @param email
     */
    @Override
    public int sendEmailCode(HttpServletRequest request, String email) {
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

}
