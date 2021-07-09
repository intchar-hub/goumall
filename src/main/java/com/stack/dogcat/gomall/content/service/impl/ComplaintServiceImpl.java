package com.stack.dogcat.gomall.content.service.impl;

import com.stack.dogcat.gomall.content.entity.Complaint;
import com.stack.dogcat.gomall.content.mapper.ComplaintMapper;
import com.stack.dogcat.gomall.content.service.IComplaintService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
