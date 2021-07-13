package com.stack.dogcat.gomall.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.entity.Complaint;
import com.stack.dogcat.gomall.content.mapper.ComplaintMapper;
import com.stack.dogcat.gomall.content.responseVo.ComplaintResponseVo;
import com.stack.dogcat.gomall.content.service.IComplaintService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 投诉表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements IComplaintService {

    @Autowired
    private StoreMapper storeMapper;
    
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ComplaintMapper complaintMapper;
    
    /**
     * 管理员查看所有投诉
     */
    @Override
    public PageResponseVo<ComplaintResponseVo> listComplaints(Integer pageNum, Integer pageSize){

        Page<Complaint> page = new Page<>(pageNum,pageSize);
        IPage<Complaint> complaintPage = complaintMapper.selectPage(page,null);
        //封装vo
        PageResponseVo<ComplaintResponseVo> complaintPageResponseVo = new PageResponseVo(complaintPage);
        List<ComplaintResponseVo> complaintVos= CopyUtil.copyList(complaintPageResponseVo.getData(), ComplaintResponseVo.class);

        //获取用户名和店铺名
        for (ComplaintResponseVo complaintVo:complaintVos) {
            complaintVo.setCustomerName(customerMapper.selectById(complaintVo.getCustomerId()).getUserName());
            complaintVo.setStoreName(storeMapper.selectById(complaintVo.getStoreId()).getStoreName());
        }
        complaintPageResponseVo.setData(complaintVos);
        return complaintPageResponseVo;

    }

    /**
     * 管理员处理投诉
     */
    @Override
    @Transactional
    public Integer solveComplaints(Integer complaintId,Integer banned){
        if(banned==0){
            return 1;
        }
        else {
            Integer storeId=complaintMapper.selectById(complaintId).getStoreId();
            UpdateWrapper<Store> storeUpdateWrapper = new UpdateWrapper<>();
            storeUpdateWrapper.eq("id",storeId).set("status",3);
            int i = storeMapper.update(null,storeUpdateWrapper);
            UpdateWrapper<Complaint> complaintUpdateWrapper = new UpdateWrapper<>();
            int j = complaintMapper.update(null,complaintUpdateWrapper);
            return i*j;
        }
    }
}
