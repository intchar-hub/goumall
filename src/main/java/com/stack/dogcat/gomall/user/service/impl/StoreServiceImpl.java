package com.stack.dogcat.gomall.user.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.entity.VerifyCode;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.user.mapper.VerifyCodeMapper;
import com.stack.dogcat.gomall.user.requestVo.StoreEmailLoginRequestVo;
import com.stack.dogcat.gomall.user.requestVo.StorePwdLoginRequestVo;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    VerifyCodeMapper verifyCodeMapper;

    @Autowired
    ProductMapper productMapper;

    /**
     * 向商家发送邮箱验证码（四位数）
     * @param email
     */
    @Override
    public void sendEmailCode(String email) {
        String emailServiceCode = String.format("%04d", new Random().nextInt(9999));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("验证码");
        message.setText("验证码（五分钟内有效）：" + emailServiceCode);
        message.setFrom("w741008w@163.com");
        message.setTo(email);
        mailSender.send(message);
        LOG.info("商家" + email + "验证码：" + emailServiceCode);

        //字符验证码存入数据库
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mark_string", email);
        verifyCodeMapper.delete(queryWrapper);

        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setMarkString(email);
        verifyCode.setVerifyCode(emailServiceCode);
        verifyCode.setGmtCreate(LocalDateTime.now());
        verifyCodeMapper.insert(verifyCode);
    }

    /**
     * 商家注册
     * @param registerRequestVo
     */
    @Override
    public void register(StoreRegisterRequestVo registerRequestVo) {

        //判断邮箱验证码是否正确
        LOG.info("商家" + registerRequestVo.getEmail() + "验证码：" + registerRequestVo.getEmail());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mark_string", registerRequestVo.getEmail());
        queryWrapper.eq("verify_code", registerRequestVo.getVerifyCode());
        List<VerifyCode> verifyCodesDB = verifyCodeMapper.selectList(queryWrapper);
        if(verifyCodesDB == null || verifyCodesDB.size() == 0) {
            throw new RuntimeException("验证码错误或已失效，请重试！");
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
        store.setAvatarPath("https://baomidou.com/img/logo.svg");

        storeMapper.insert(store);
    }

    /**
     * 商家获取字符验证码
     * @param response
     * @param markString
     */
    @Override
    public void getStringCode(HttpServletResponse response, String markString) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(90, 40);

        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        LOG.info("商家" + markString + "验证码：" + lineCaptcha.getCode());
        try {
            lineCaptcha.write(response.getOutputStream());

            //字符验证码放入数据库
            VerifyCode verifyCode = new VerifyCode();
            verifyCode.setMarkString(markString);
            verifyCode.setVerifyCode(lineCaptcha.getCode());
            verifyCode.setGmtCreate(LocalDateTime.now());
            verifyCodeMapper.insert(verifyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商家密码登录
     * @param requestVo
     * @return
     */
    @Override
    public StoreLoginResponseVo pwdLogin(StorePwdLoginRequestVo requestVo) {

        //判断字符验证码是否正确
        LOG.info("商家" + requestVo.getMarkString() + "验证码：" + requestVo.getVerifyCode());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mark_string", requestVo.getMarkString());
        queryWrapper.eq("verify_code", requestVo.getVerifyCode());
        List<VerifyCode> verifyCodesDB = verifyCodeMapper.selectList(queryWrapper);
        if(verifyCodesDB == null || verifyCodesDB.size() == 0) {
            throw new RuntimeException("验证码错误或已失效，请重试！");
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", requestVo.getUserName());
        Store storeDB = storeMapper.selectOne(queryWrapper);
        if(storeDB.getStatus() != 1) {
            throw new RuntimeException("该用户不存在");
        }
        if(storeDB != null && storeDB.getPassword().equals(requestVo.getPassword())) {
            StoreLoginResponseVo responseVo = CopyUtil.copy(storeDB, StoreLoginResponseVo.class);
            return responseVo;
        } else {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    /**
     * 商家邮箱登录
     * @param requestVo
     * @return
     */
    @Override
    public StoreLoginResponseVo emailLogin(StoreEmailLoginRequestVo requestVo) {

        //判断邮箱验证码是否正确
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mark_string", requestVo.getEmail());
        queryWrapper.eq("verify_code", requestVo.getVerifyCode());
        List<VerifyCode> verifyCodesDB = verifyCodeMapper.selectList(queryWrapper);
        if(verifyCodesDB == null || verifyCodesDB.size() == 0) {
            throw new RuntimeException("验证码错误或已失效，请重试！");
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", requestVo.getEmail());
        Store storeDB = storeMapper.selectOne(queryWrapper);
        if(storeDB.getStatus() != 1) {
            throw new RuntimeException("该用户不存在");
        }
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
        store.setAvatarPath(requestVo.getAvatarPath());
        store.setShipAddress(requestVo.getShipAddress());
        store.setDescription(requestVo.getDescription());

        if(requestVo.getPassword() != null && !requestVo.getPassword().isEmpty() && !requestVo.getPassword().equals("")) {
            store.setPassword(requestVo.getPassword());
        }

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
        List<StoreQueryResponseVo> storeQueryResponseVos = new ArrayList<>();

        for (Store record : storeIPage.getRecords()) {
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("status", 1);
            queryWrapper.eq("store_id", record.getId());
            queryWrapper.orderByDesc("sales_num");
            List<Product> productsDB = productMapper.selectList(queryWrapper);

            List<String> imagePaths = new ArrayList<>();
            if(productsDB.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    imagePaths.add(productsDB.get(i).getImagePath());
                }
            } else {
                String[] paths = {"http://qwmwlt898.hd-bkt.clouddn.com/Ft12snMfhT-a_alm4Pst41Sgqu1H",
                "http://qwmwlt898.hd-bkt.clouddn.com/Fg-sBXH95t3a9CiDuhsLUJ9jtoQb",
                "http://qwmwlt898.hd-bkt.clouddn.com/FodHvEC2_8F2You0Wpe1PmS0OvHi"};
                for (int i = 0; i < productsDB.size(); i++) {
                    imagePaths.add(productsDB.get(i).getImagePath());
                }
                for (int i = 0; i < 3 - productsDB.size(); i++) {
                    imagePaths.add(paths[i]);
                }
            }

            StoreQueryResponseVo vo = new StoreQueryResponseVo();
            vo = CopyUtil.copy(record, StoreQueryResponseVo.class);
            vo.setImagePaths(imagePaths);

            storeQueryResponseVos.add(vo);
        }

        responseVo.setData(storeQueryResponseVos);

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
        }, 5 * 60 * 1000);
    }

}
