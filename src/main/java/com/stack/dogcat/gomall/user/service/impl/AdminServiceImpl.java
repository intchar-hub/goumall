package com.stack.dogcat.gomall.user.service.impl;

import com.stack.dogcat.gomall.user.entity.Admin;
import com.stack.dogcat.gomall.user.mapper.AdminMapper;
import com.stack.dogcat.gomall.user.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员信息表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
