package com.stack.dogcat.gomall.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.stack.dogcat.gomall.user.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.vo.StoreInfoResponseVo;

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

    IPage<StoreInfoResponseVo> listStoreInfo(int pageNum, int pageSize);

}
