package com.stack.dogcat.gomall.content.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.entity.Complaint;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.content.responseVo.ComplaintQueryResponseVo;
import com.stack.dogcat.gomall.content.responseVo.ComplaintResponseVo;

import java.util.List;

/**
 * <p>
 * 投诉表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IComplaintService extends IService<Complaint> {

    PageResponseVo<ComplaintResponseVo> listComplaints(Integer pageNum, Integer pageSize);

    Integer solveComplaints(Integer complaintId,Integer banned);

    void saveComplaint(Integer customerId, Integer storeId, String content);

    List<ComplaintQueryResponseVo> listComplaintsByCustomer(Integer customerId);

    void deleteComplaint(Integer customerId, Integer complaintId);
}
