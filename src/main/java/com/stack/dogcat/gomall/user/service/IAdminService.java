package com.stack.dogcat.gomall.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.entity.Admin;
import com.stack.dogcat.gomall.user.requestVo.AdminEmailLoginRequestVo;
import com.stack.dogcat.gomall.user.requestVo.AdminPwdLoginRequestVo;
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
    PageResponseVo<StoreInfoResponseVo> listStoreInfoByStatus(Integer pageNum, Integer pageSize);
    Integer examineStoreRegister(Integer id,Integer flag);
    Integer sendEmailCode(String email);
    void getStringCode(HttpServletResponse response);
    AdminLoginResponseVo emailLogin(AdminEmailLoginRequestVo adminEmailLoginRequestVo);
    AdminLoginResponseVo pwdLogin(AdminPwdLoginRequestVo adminPwdLoginRequestVo);

}
