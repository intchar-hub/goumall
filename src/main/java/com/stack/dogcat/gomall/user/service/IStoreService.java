package com.stack.dogcat.gomall.user.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.entity.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.user.requestVo.StoreEmailLoginRequestVo;
import com.stack.dogcat.gomall.user.requestVo.StorePwdLoginRequestVo;
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

    void sendEmailCode(String email);

    void register(StoreRegisterRequestVo registerRequestVo);

    void getStringCode(HttpServletResponse response, String userName);

    StoreLoginResponseVo pwdLogin(StorePwdLoginRequestVo requestVo);

    StoreLoginResponseVo emailLogin(StoreEmailLoginRequestVo requestVo);

    void updateStoreInfo(StoreUpdateInfoRequestVo requestVo);

    StoreInfoQueryResponseVo getStoreInfo(Integer id);

    void deleteStore(Integer id, String password);

    PageResponseVo<StoreQueryResponseVo> listStoresByName(String name, Integer pageNum, Integer pageSize);
}
