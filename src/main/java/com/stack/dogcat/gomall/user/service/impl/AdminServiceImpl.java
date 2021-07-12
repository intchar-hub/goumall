package com.stack.dogcat.gomall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.entity.Admin;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.AdminMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.user.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.vo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 管理员信息表 服务实现类
 * </p>
 *
 * @author xrm
 * @update kxy
 * @since 2021-07-08
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 管理员查看所有商家信息
     */
    @Override
    public PageResponseVo<StoreInfoResponseVo> listStoreInfo(int pageNum, int pageSize){

        Page<Store> page = new Page<>(pageNum,pageSize);
        IPage<Store> storePage = storeMapper.selectPage(page,null);
        PageResponseVo<StoreInfoResponseVo> storePageResponseVo = new PageResponseVo(storePage);
        storePageResponseVo.setData(CopyUtil.copyList(storePageResponseVo.getData(), StoreInfoResponseVo.class));
        return storePageResponseVo;

    }

}
