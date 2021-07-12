package com.stack.dogcat.gomall.user.service;

import com.stack.dogcat.gomall.user.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.user.vo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;

import java.util.List;

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

}
