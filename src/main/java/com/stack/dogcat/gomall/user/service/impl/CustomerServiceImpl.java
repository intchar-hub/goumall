package com.stack.dogcat.gomall.user.service.impl;

import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import com.stack.dogcat.gomall.user.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
