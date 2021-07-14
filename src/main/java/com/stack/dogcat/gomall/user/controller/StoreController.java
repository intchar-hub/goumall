package com.stack.dogcat.gomall.user.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.user.requestVo.StoreRegisterRequestVo;
import com.stack.dogcat.gomall.user.requestVo.StoreUpdateInfoRequestVo;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoQueryResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreLoginResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreQueryResponseVo;
import com.stack.dogcat.gomall.user.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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
     * 商家获取字符验证码
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
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    /**
     * 商家密码登录
     * @param userName
     * @param password
     * @param verifyString
     * @return
     */
    @PostMapping("/pwdLogin")
    public SysResult pwdLogin(HttpServletRequest request, String userName, String password, String verifyString) {
        StoreLoginResponseVo responseVo = null;
        try {
            responseVo= storeService.pwdLogin(request, userName, password, verifyString);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success(responseVo);
    }

    /**
     * 商家邮箱验证码登录
     * @param email
     * @param verifyCode
     * @return
     */
    @PostMapping("/emailLogin")
    public SysResult emailLogin(HttpServletRequest request, String email, String verifyCode) {
        StoreLoginResponseVo responseVo = null;
        try {
            responseVo = storeService.emailLogin(request, email, verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success(responseVo);
    }

    /**
     * 商家更新个人/店铺信息
     * @param requestVo
     * @return
     */
    @PostMapping("/updateStoreInfo")
    public SysResult updateStoreInfo(@RequestBody StoreUpdateInfoRequestVo requestVo) {
        try {
            storeService.updateStoreInfo(requestVo);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("更新失败");
        }
        return SysResult.success();
    }

    /**
     * 获取商家信息
     * @param id
     * @return
     */
    @GetMapping("/getStoreInfo")
    public SysResult getStoreInfo(Integer id) {
        StoreInfoQueryResponseVo responseVo = null;
        try {
            responseVo = storeService.getStoreInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success(responseVo);
    }

    /**
     * 商家删除/注销账号
     * @param id
     * @param password
     * @return
     */
    @DeleteMapping("/deleteStore")
    public SysResult deleteStore(Integer id, String password) {
        try {
            storeService.deleteStore(id, password);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    /**
     * 用户按名字查询店铺
     * @param name
     * @return
     */
    @GetMapping("/listStoresByName")
    public SysResult listStoresByName(String name) {
        List<StoreQueryResponseVo> responseVos = null;
        try {
            responseVos = storeService.listStoresByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("查询店铺失败");
        }
        return SysResult.success(responseVos);
    }
}
