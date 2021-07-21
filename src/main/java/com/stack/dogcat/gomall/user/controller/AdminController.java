package com.stack.dogcat.gomall.user.controller;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.user.requestVo.AdminEmailLoginRequestVo;
import com.stack.dogcat.gomall.user.requestVo.AdminPwdLoginRequestVo;
import com.stack.dogcat.gomall.user.responseVo.AdminLoginResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.user.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>
 * 管理员信息表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/user/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;


    /**
     * 管理员查看所有商家信息
     * @param pageNum
     * @param pageSize
     */
    @GetMapping("/listStoreInfo")
    public SysResult listStoreInfo(Integer pageNum,Integer pageSize){

        SysResult result=null;
        PageResponseVo<StoreInfoResponseVo> responseVo = null;
        try{
            responseVo=adminService.listStoreInfo(pageNum,pageSize);
            result = SysResult.success(responseVo);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            result = SysResult.error("获取数据失败");
        }
        return result;
    }
    
    /**
     * 管理员审核商家注册申请
     *@param id
     *@param flag
     */
    @PostMapping("/examineStoreRegister")
    public SysResult examineStoreRegister(Integer id,Integer flag){

        SysResult result=null;
        try{
            Integer i=adminService.examineStoreRegister(id,flag);
            if(i==1){
                result = SysResult.success();
            }
            else{
                result = SysResult.error("审核处理失败");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            result = SysResult.error("发生未知异常");
        }
        return result;
    }

    /**
     * 管理员获取图片验证码
     * @param response
     */
    @GetMapping("/getStringCode")
    public void getStringCode(HttpServletResponse response) {
        try {
            adminService.getStringCode(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 管理员获取邮箱验证码
     * @param email
     */
    @GetMapping("/getEmailCode")
    public SysResult getEmailCode(String email) {
        try {
            Integer flag=adminService.sendEmailCode(email);
            if(flag==0){
                return SysResult.error("改邮箱未注册");
            }
            else{
                return SysResult.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("验证码发送失败");
        }
    }

    /**
     * 管理员邮箱验证码登录
     */
    @PostMapping("/emailLogin")
    public SysResult emailLogin(@Valid @RequestBody AdminEmailLoginRequestVo adminEmailLoginRequestVo) {
        AdminLoginResponseVo responseVo = null;
        try {
            responseVo = adminService.emailLogin(adminEmailLoginRequestVo);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success(responseVo);
    }

    /**
     * 管理员密码登录
     */
    @PostMapping("/pwdLogin")
    public SysResult pwdLogin(@Valid @RequestBody AdminPwdLoginRequestVo adminPwdLoginRequestVo) {
        AdminLoginResponseVo responseVo = null;
        try {
            responseVo = adminService.pwdLogin(adminPwdLoginRequestVo);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success(responseVo);
    }

}
