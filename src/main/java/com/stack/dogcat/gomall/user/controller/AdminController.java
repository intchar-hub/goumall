package com.stack.dogcat.gomall.user.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.user.responseVo.ComplaintResponseVo;
import com.stack.dogcat.gomall.user.service.IAdminService;
import com.stack.dogcat.gomall.user.responseVo.StoreInfoResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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


    /**
     * 管理员查看所有商家信息
     */
    @GetMapping("/listStoreInfo")
    public SysResult listStoreInfo(int pageNum,int pageSize){

        SysResult result=null;
        PageResponseVo<StoreInfoResponseVo> storeInfoPage = null;
        try{
            storeInfoPage=adminService.listStoreInfo(pageNum,pageSize);
            result = SysResult.success(storeInfoPage);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            result = SysResult.error("获取数据失败");
        }
        return result;
    }


    /**
     * 管理员查看所有投诉
     */
    @GetMapping("/listComplaints")
    public SysResult listComplaints(int pageNum,int pageSize){

        SysResult result=null;
        PageResponseVo<ComplaintResponseVo> compliantPage = null;
        try{
            compliantPage=adminService.listComplaints(pageNum,pageSize);
            result = SysResult.success(compliantPage);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            result = SysResult.error("获取数据失败");
        }
        return result;
    }

    /**
     * 管理员处理投诉
     */
    @PostMapping("/solveComplaints")
    public SysResult solveComplaints(int complaintId,int banned){

        SysResult result=null;
        try{
            int flag=adminService.solveComplaints(complaintId,banned);
            if(flag==1){
                result = SysResult.success();
            }
            else{
                result = SysResult.error("封禁店铺失败");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            result = SysResult.error("发生未知异常");
        }
        return result;
    }

    /**
     * 管理员审核商家注册申请
     */
    @PostMapping("/examineStoreRegister")
    public SysResult examineStoreRegister(int id,int flag){

        SysResult result=null;
        try{
            int i=adminService.examineStoreRegister(id,flag);
            if(i==1){
                result = SysResult.success();
            }
            else{
                result = SysResult.error("审核处理失败");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            result = SysResult.error("发生未知异常");
        }
        return result;
    }
}
