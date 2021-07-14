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
    public void insertReceiveAddressByCustomerId(Integer customerId,String address);

    //修改收货地址
    public void updateReceiveAddressById(ReceiveAddress receiveAddress);

    //用id查询收货地址
    public ReceiveAddress queryReceiveAddressByReceiveAddressId(Integer receiveAddressId);

    //查询所有收货地址
    public List<ReceiveAddressQueryResponseVo> queryReceiveAddressByCustomerId(Integer customerId);
}
