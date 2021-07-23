package com.stack.dogcat.gomall.content.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.content.responseVo.ComplaintQueryResponseVo;
import com.stack.dogcat.gomall.content.responseVo.ComplaintResponseVo;
import com.stack.dogcat.gomall.content.service.IComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 投诉表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
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
    public SysResult solveComplaints(String complaintId, String banned){

        SysResult result=null;
        try{
            Integer flag=complaintService.solveComplaints(Integer.valueOf(complaintId),Integer.valueOf(banned));
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
     * 顾客投诉商家
     * @param customerId
     * @param storeId
     * @param content
     * @return
     */
    @PostMapping("/saveComplaint")
    public SysResult saveComplaint(Integer customerId, Integer storeId, String content) {
        try {
            complaintService.saveComplaint(customerId, storeId, content);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("投诉失败");
        }
        return SysResult.success();
    }

    /**
     * 顾客查看投诉
     * @param customerId
     * @return
     */
    @GetMapping("/listComplaintsByCustomer")
    public SysResult listComplaintsByCustomer(Integer customerId) {
        List<ComplaintQueryResponseVo> responseVos = null;
        try {
            responseVos = complaintService.listComplaintsByCustomer(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("投诉信息获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 顾客撤销投诉
     * @param customerId
     * @param complaintId
     * @return
     */
    @PostMapping("/deleteComplaint")
    public SysResult deleteComplaint(Integer customerId, Integer complaintId) {
        try {
            complaintService.deleteComplaint(customerId, complaintId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("撤销投诉失败");
        }
        return SysResult.success();
    }

}
