package com.stack.dogcat.gomall.user.service;

import com.stack.dogcat.gomall.user.entity.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.user.requestVo.StoreRegisterRequestVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * <p>
 * 商店信息表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IStoreService extends IService<Store> {

    void sendEmailCode(HttpServletRequest request, String email);

    void register(HttpServletRequest request, StoreRegisterRequestVo registerRequestVo);

    void getStringCode(HttpServletRequest request, HttpServletResponse response, String userName);
}
