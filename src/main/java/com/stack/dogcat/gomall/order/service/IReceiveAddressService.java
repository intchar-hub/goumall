package com.stack.dogcat.gomall.order.service;

import com.stack.dogcat.gomall.order.entity.ReceiveAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.order.responseVo.ReceiveAddressQueryResponseVo;

import java.util.List;

/**
 * <p>
 * 收货地址表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IReceiveAddressService extends IService<ReceiveAddress> {

    //添加收货地址
    void insertReceiveAddressByCustomerId(Integer customerId,String consignee,String address,String phoneNumber);

    //修改收货地址
    void updateReceiveAddressById(Integer receiveAddressId,ReceiveAddress receiveAddress);

    //用id查询收货地址
    ReceiveAddress queryReceiveAddressByReceiveAddressId(Integer receiveAddressId);

    //查询所有收货地址
    List<ReceiveAddressQueryResponseVo> queryReceiveAddressByCustomerId(Integer customerId);

    //根据ReceiveAddressId删除收货地址
    void deleteReceiveAddressById(Integer receiveAddressId);

    //设置默认地址
    void updateDefaultAddressById(Integer customerId,Integer receiveAddressId,Integer defaultAddress);
}
