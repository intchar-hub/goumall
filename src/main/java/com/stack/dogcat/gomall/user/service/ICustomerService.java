package com.stack.dogcat.gomall.user.service;

import com.stack.dogcat.gomall.user.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface ICustomerService extends IService<Customer> {
    //根据id查询消费者
    Customer queryCustomerByCustomerId(String customerid);

    //根据openid查询消费者
    Customer queryCustomerByOpenid(String openid);

    //插入消费者信息
    void insertCustomer(String openid,String sessionKey,String userName,String avatarPath,Integer gender);

    //根据id更新消费者信息
    void updateCustomerInfoById(Customer customer);


}
