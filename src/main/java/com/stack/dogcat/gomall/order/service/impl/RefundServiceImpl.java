package com.stack.dogcat.gomall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.order.entity.Refund;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.order.mapper.RefundMapper;
import com.stack.dogcat.gomall.order.responseVo.RefundResponseVo;
import com.stack.dogcat.gomall.order.service.IRefundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 退款表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements IRefundService {

    @Autowired
    RefundMapper refundMapper;

    @Autowired
    OrderMapper orderMapper;

    @Override
    public void saveRefund(Integer orderId,String reason){

        Refund refund = new Refund();
        refund.setOrderId(orderId);
        refund.setStoreId(orderMapper.selectById(orderId).getStoreId());
        refund.setReason(reason);
        refund.setGmtCreate(LocalDateTime.now());

        refundMapper.insert(refund);
    }

    @Override
    public PageResponseVo<RefundResponseVo> listRefundByStoreId(Integer storeId, Integer status, Integer pageNum, Integer pageSize){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id",storeId);
        queryWrapper.orderByDesc("gmt_create");
        if(status==4){
            queryWrapper.in("status", 2,3);
        }else{
            queryWrapper.eq("status",status);
        }

        Page<Refund>page=new Page<>(pageNum,pageSize);
        IPage<Refund>refundIPage=refundMapper.selectPage(page,queryWrapper);
        PageResponseVo<RefundResponseVo>responseVo=new PageResponseVo(refundIPage);
        responseVo.setData(CopyUtil.copyList(refundIPage.getRecords(),RefundResponseVo.class));

        return responseVo;
    }

    @Override
    @Transactional
    public void handleRefundApply(Integer refundId,Integer option){


    }



}
