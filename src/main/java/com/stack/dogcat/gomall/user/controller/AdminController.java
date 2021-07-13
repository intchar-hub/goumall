package com.stack.dogcat.gomall.user.controller;


import cn.hutool.captcha.LineCaptcha;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.service.IAdminService;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 管理员信息表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/user/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    LineCaptcha lineCaptcha;


    /**
     * 管理员查看所有商家信息
     * @param pageNum
     * @param pageSize
     */
    @GetMapping("/listStoreInfo")
    public SysResult listStoreInfo(int pageNum,int pageSize){

        SysResult result=null;
        PageResponseVo<StoreInfoResponseVo> storeInfoPage = null;
        try{
            storeInfoPage=adminService.listStoreInfo(pageNum,pageSize);
            result = SysResult.success(storeInfoPage);
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
    public SysResult examineStoreRegister(int id,int flag){

        SysResult result=null;
        try{
            int i=adminService.examineStoreRegister(id,flag);
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
     * @param request
     * @param response
     */
    @GetMapping("/getStringCode")
    public void getStringCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            adminService.getStringCode(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 管理员获取邮箱验证码
     * @param email
     * @param request
     */
    @GetMapping("/getEmailCode")
    public SysResult getEmailCode(HttpServletRequest request, String email) {
        try {
            int flag=adminService.sendEmailCode(request, email);
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

}
