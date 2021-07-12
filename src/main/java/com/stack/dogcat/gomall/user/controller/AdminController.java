package com.stack.dogcat.gomall.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.service.IAdminService;
import com.stack.dogcat.gomall.user.vo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.utils.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 管理员信息表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/user/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @GetMapping("/listStoreInfo")
    public SysResult listStoreInfo(int pageNum,int pageSize){

        SysResult result=new SysResult();
        PageResponseVo<StoreInfoResponseVo> storeInfoPage = null;
        try{
            storeInfoPage=adminService.listStoreInfo(pageNum,pageSize);
            result = SysResult.build(200,"操作成功",storeInfoPage);
        }
        catch (Exception e){
            result = SysResult.error(400, e.getMessage());
        }
        return result;
    }

}
