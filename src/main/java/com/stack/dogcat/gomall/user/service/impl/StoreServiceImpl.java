package com.stack.dogcat.gomall.user.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.user.requestVo.StoreRegisterRequestVo;
import com.stack.dogcat.gomall.user.requestVo.StoreUpdateInfoRequestVo;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoQueryResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreLoginResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreQueryResponseVo;
import com.stack.dogcat.gomall.user.service.IStoreService;
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
import java.time.LocalDateTime;
import java.util.List;
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

    private static final Logger LOG = LoggerFactory.getLogger(StoreServiceImpl.class);

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
        message.setSubject("验证码");
        message.setText("验证码（五分钟内有效）：" + emailServiceCode);
        message.setFrom("w741008w@163.com");
        message.setTo(email);
        mailSender.send(message);
        LOG.info("商家" + email + "验证码：" + emailServiceCode);

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

        //判断邮箱验证码是否正确
        HttpSession session = request.getSession();
        String correctCode = (String)session.getAttribute("store-" + registerRequestVo.getEmail());
        LOG.info("商家" + registerRequestVo.getEmail() + "验证码：" + correctCode);
        if(correctCode == null || correctCode.isEmpty()) {
            throw new RuntimeException("验证码已失效");
        }
        if(!correctCode.equals(registerRequestVo.getVerifyCode())) {
            throw new RuntimeException("验证码错误");
        }

        /**
         * 判断用户名是否唯一
         */
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name", registerRequestVo.getUserName());
        List<Store> storeDB = storeMapper.selectList(wrapper);
        if(storeDB != null && storeDB.size() > 0) {
            throw new RuntimeException("用户名已存在");
        }

        /**
         * 判断邮箱是否唯一
         */
        wrapper = new QueryWrapper();
        wrapper.eq("email", registerRequestVo.getEmail());
        storeDB = storeMapper.selectList(wrapper);
        if(storeDB != null && storeDB.size() > 0) {
            throw new RuntimeException("邮箱已注册");
        }

        /**
         * 判断店铺名是否唯一
         */
        wrapper = new QueryWrapper();
        wrapper.eq("store_name", registerRequestVo.getStoreName());
        storeDB = storeMapper.selectList(wrapper);
        if(storeDB != null && storeDB.size() > 0) {
            throw new RuntimeException("店铺名已存在");
        }

        Store store = new Store();
        store.setUserName(registerRequestVo.getUserName());
        store.setStoreName(registerRequestVo.getStoreName());
        store.setPassword(registerRequestVo.getPassword());
        store.setEmail(registerRequestVo.getEmail());
        store.setGmtCreate(LocalDateTime.now());
        store.setStatus(0);
        store.setFansNum(0);
//        store.setAvatarPath("https://baomidou.com/img/logo.svg");

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
        LOG.info("商家" + userName + "验证码：" + lineCaptcha.getCode());
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
     * 商家密码登录
     * @param userName
     * @param password
     * @param verifyString
     * @return
     */
    @Override
    public StoreLoginResponseVo pwdLogin(HttpServletRequest request, String userName, String password, String verifyString) {

        //判断字符验证码是否正确
        HttpSession session = request.getSession();
        String correctCode = (String)session.getAttribute("store-" + userName);
        LOG.info("商家" + userName + "验证码：" + correctCode);
        if(correctCode == null || correctCode.isEmpty()) {
            throw new RuntimeException("验证码已失效");
        }
        if(!correctCode.equals(verifyString)) {
            throw new RuntimeException("验证码错误");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", userName);
        Store storeDB = storeMapper.selectOne(queryWrapper);
        if(storeDB != null && storeDB.getPassword().equals(password)) {
            StoreLoginResponseVo responseVo = CopyUtil.copy(storeDB, StoreLoginResponseVo.class);
            return responseVo;
        } else {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    /**
     * 商家邮箱登录
     * @param request
     * @param email
     * @param verifyCode
     * @return
     */
    @Override
    public StoreLoginResponseVo emailLogin(HttpServletRequest request, String email, String verifyCode) {

        //判断邮箱验证码是否正确
        HttpSession session = request.getSession();
        String correctCode = (String)session.getAttribute("store-" + email);
        LOG.info("商家" + email + "验证码：" + correctCode);
        if(correctCode == null || correctCode.isEmpty()) {
            throw new RuntimeException("验证码已失效或邮箱错误");
        }
        if(!correctCode.equals(verifyCode)) {
            throw new RuntimeException("验证码错误");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        Store storeDB = storeMapper.selectOne(queryWrapper);
        if(storeDB != null) {
            StoreLoginResponseVo responseVo = CopyUtil.copy(storeDB, StoreLoginResponseVo.class);
            return responseVo;
        } else {
            throw new RuntimeException("邮箱错误");
        }
    }

    /**
     * 商家修改个人/店铺信息
     * @param requestVo
     */
    @Override
    public void updateStoreInfo(StoreUpdateInfoRequestVo requestVo) {

        Store store = new Store();
        store.setId(requestVo.getId());
        store.setPassword(requestVo.getPassword());
        store.setAvatarPath(requestVo.getAvatarPath());
        store.setShipAddress(requestVo.getShipAddress());

        storeMapper.updateById(store);
    }

    /**
     * 商家查询个人信息
     * @param id
     * @return
     */
    @Override
    public StoreInfoQueryResponseVo getStoreInfo(Integer id) {
        if(id == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        Store storeDB = storeMapper.selectById(id);
        if(storeDB == null) {
            throw new RuntimeException("该商家不存在");
        }

        StoreInfoQueryResponseVo responseVo = CopyUtil.copy(storeDB, StoreInfoQueryResponseVo.class);
        return responseVo;
    }

    /**
     * 商家删除/注销账号
     * @param id
     * @param password
     */
    @Override
    public void deleteStore(Integer id, String password) {
        Store storeDB = storeMapper.selectById(id);
        if(storeDB == null) {
            throw new RuntimeException("该商家不存在");
        }
        if(!storeDB.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        storeDB.setStatus(4);
        storeMapper.updateById(storeDB);
    }

    /**
     * 用户按名字查询店铺(%店%铺%名%)
     * @param name
     * @return
     */
    @Override
    public PageResponseVo<StoreQueryResponseVo> listStoresByName(String name, Integer pageNum, Integer pageSize) {
        if(name == null || pageNum == null || pageSize == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        String likeName = "";
        if(name != null) {
            String[] split = name.split("");
            for (int i = 0; i < split.length; i++) {
                likeName += split[i];
                if(i != split.length - 1) {
                    likeName += "%";
                }
            }
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("store_name", likeName);
        queryWrapper.eq("status", 1);

        Page<Store> page = new Page<>(pageNum, pageSize);
        IPage<Store> storeIPage = storeMapper.selectPage(page, queryWrapper);
        PageResponseVo<StoreQueryResponseVo> responseVo = new PageResponseVo(storeIPage);
        responseVo.setData(CopyUtil.copyList(storeIPage.getRecords(), StoreQueryResponseVo.class));

        return responseVo;
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
