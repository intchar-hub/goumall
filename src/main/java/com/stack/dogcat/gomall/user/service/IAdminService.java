package com.stack.dogcat.gomall.user.service;

import com.stack.dogcat.gomall.user.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.user.responseVo.ComplaintResponseVo;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;

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
    PageResponseVo<ComplaintResponseVo> listComplaints(int pageNum,int pageSize);

}
