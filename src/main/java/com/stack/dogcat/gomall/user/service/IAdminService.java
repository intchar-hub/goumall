package com.stack.dogcat.gomall.user.service;

import com.stack.dogcat.gomall.user.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;

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

    PageResponseVo<StoreInfoResponseVo> listStoreInfo(int pageNum, int pageSize);
    int examineStoreRegister(int id,int flag);
    int sendEmailCode(HttpServletRequest request, String email);
    void getStringCode(HttpServletRequest request, HttpServletResponse response);

}
