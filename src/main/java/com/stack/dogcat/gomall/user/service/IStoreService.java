package com.stack.dogcat.gomall.user.service;

import com.stack.dogcat.gomall.user.entity.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.user.requestVo.StoreRegisterRequestVo;
import com.stack.dogcat.gomall.user.requestVo.StoreUpdateInfoRequestVo;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoQueryResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreLoginResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreQueryResponseVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    StoreLoginResponseVo pwdLogin(HttpServletRequest request, String userName, String password, String verifyString);

    StoreLoginResponseVo emailLogin(HttpServletRequest request, String email, String verifyCode);

    void updateStoreInfo(StoreUpdateInfoRequestVo requestVo);

    StoreInfoQueryResponseVo getStoreInfo(Integer id);

    void deleteStore(Integer id, String password);

    List<StoreQueryResponseVo> listStoresByName(String name);
}
