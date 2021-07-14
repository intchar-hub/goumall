package com.stack.dogcat.gomall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.stack.dogcat.gomall.order.entity.ReceiveAddress;
import com.stack.dogcat.gomall.order.mapper.ReceiveAddressMapper;
import com.stack.dogcat.gomall.order.responseVo.ReceiveAddressQueryResponseVo;
import com.stack.dogcat.gomall.order.service.IReceiveAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 收货地址表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ReceiveAddressServiceImpl extends ServiceImpl<ReceiveAddressMapper, ReceiveAddress> implements IReceiveAddressService {

    @Autowired
    ReceiveAddressMapper receiveAddressMapper;

    @Override
    public void insertReceiveAddressByCustomerId(Integer customerId,String address){
        ReceiveAddress receiveAddress=new ReceiveAddress();
        receiveAddress.setCustomerId(customerId);
        receiveAddress.setAddress(address);

        QueryWrapper<ReceiveAddress> wrapper = new QueryWrapper<ReceiveAddress>();
        wrapper.eq("customer_id", customerId);
        ReceiveAddress receiveAddress1=null;
        receiveAddress1 = receiveAddressMapper.selectOne(wrapper);
        if(receiveAddress1==null){
            //无收货地址
            receiveAddress.setDefaultAddress(1);
            receiveAddressMapper.insert(receiveAddress);
        }
        else{
            //已有收货地址
            receiveAddressMapper.insert(receiveAddress);
        }
    }

    @Override
    public void updateReceiveAddressById(ReceiveAddress receiveAddress) {

        receiveAddressMapper.updateById(receiveAddress);

    }

    @Override
    public ReceiveAddress queryReceiveAddressByReceiveAddressId(Integer receiveAddressId){

        ReceiveAddress receiveAddress=receiveAddressMapper.selectById(receiveAddressId);
        return receiveAddress;
    }

    @Override
    public List<ReceiveAddressQueryResponseVo> queryReceiveAddressByCustomerId(Integer customerId){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id", customerId);
        List<ReceiveAddressQueryResponseVo>receiveAddress_list =receiveAddressMapper.selectList(queryWrapper);
        return  receiveAddress_list;
    }
}
