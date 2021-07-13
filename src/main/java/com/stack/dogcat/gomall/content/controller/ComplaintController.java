package com.stack.dogcat.gomall.content.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.content.responseVo.ComplaintResponseVo;
import com.stack.dogcat.gomall.content.service.IComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 投诉表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/cms/complaint")
public class ComplaintController {

    @Autowired
    IComplaintService complaintService;

    /**
     * 管理员查看所有投诉
     * @param pageNum
     * @param pageSize
     */
    @GetMapping("/listComplaints")
    public SysResult listComplaints(Integer pageNum,Integer pageSize){

        SysResult result=null;
        PageResponseVo<ComplaintResponseVo> compliantPage = null;
        try{
            compliantPage=complaintService.listComplaints(pageNum,pageSize);
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
     * @param complaintId
     * @param banned
     */
    @PostMapping("/solveComplaints")
    public SysResult solveComplaints(Integer complaintId, Integer banned){

        SysResult result=null;
        try{
            Integer flag=complaintService.solveComplaints(complaintId,banned);
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

}
