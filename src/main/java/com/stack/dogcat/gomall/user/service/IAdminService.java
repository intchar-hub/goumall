package com.stack.dogcat.gomall.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.entity.Admin;
import com.stack.dogcat.gomall.user.responseVo.AdminLoginResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoResponseVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 管理员信息表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IAdminService extends IService<Admin> {

    PageResponseVo<StoreInfoResponseVo> listStoreInfo(Integer pageNum, Integer pageSize);
    Integer examineStoreRegister(Integer id,Integer flag);
    Integer sendEmailCode(HttpServletRequest request, String email);
    void getStringCode(HttpServletRequest request, HttpServletResponse response);
    AdminLoginResponseVo emailLogin(HttpServletRequest request, String email, String verifyCode);
    AdminLoginResponseVo pwdLogin(HttpServletRequest request, String userName, String password, String verifyString);

}
