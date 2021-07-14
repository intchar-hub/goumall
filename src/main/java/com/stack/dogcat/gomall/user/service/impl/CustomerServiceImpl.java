package com.stack.dogcat.gomall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import com.stack.dogcat.gomall.user.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public Customer queryCustomerByCustomerId(String customerid) {
        Customer customer = null;
        //1)构建QueryWrapper条件封装器
        QueryWrapper<Customer> wrapper = new QueryWrapper<Customer>();
        wrapper.eq("id", customerid);
        //2)userMapper执行查询
        customer = customerMapper.selectOne(wrapper);
        //3)返回结果
        return customer;


    }

    @Override
    public Customer queryCustomerByOpenid(String openid) {
        Customer customer = null;
        //1)构建QueryWrapper条件封装器
        QueryWrapper<Customer> wrapper = new QueryWrapper<Customer>();
        wrapper.eq("open_id", openid);
        //2)userMapper执行查询
        customer = customerMapper.selectOne(wrapper);
        //3)返回结果
        return customer;
    }

    @Override
    public void insertCustomer(String openid,String sessionKey,String userName,String avatarPath,Integer gender){
        Customer customer =new Customer();
        customer.setOpenId(openid);
        customer.setSessionKey(sessionKey);
        customer.setUserName(userName);
        customer.setAvatorPath(avatarPath);
        customer.setGender(gender);
        customer.setDeleted(0);

        customerMapper.insert(customer);
    }

    @Override
    public void updateCustomerInfoById(Customer customer){
        customerMapper.updateById(customer);

    }
}
