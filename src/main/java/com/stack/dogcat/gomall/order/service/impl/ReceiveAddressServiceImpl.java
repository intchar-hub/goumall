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
import org.springframework.transaction.annotation.Transactional;

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
    public void insertReceiveAddressByCustomerId(Integer customerId,String consignee,String address,String phoneNumber){
        ReceiveAddress receiveAddress=new ReceiveAddress();
        receiveAddress.setCustomerId(customerId);
        receiveAddress.setConsignee(consignee);
        receiveAddress.setAddress(address);
        receiveAddress.setPhoneNumber(phoneNumber);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id", customerId);
        List<ReceiveAddress> receiveAddress1=null;
        receiveAddress1 = receiveAddressMapper.selectList(queryWrapper);
        System.out.println(receiveAddress1);
        if(receiveAddress1.isEmpty()||receiveAddress1==null){
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
    public void updateReceiveAddressById(Integer receiveAddressId,ReceiveAddress receiveAddress) {

        ReceiveAddress receiveAddress1 = receiveAddressMapper.selectById(receiveAddressId);
        if(receiveAddress.getConsignee()!=null){receiveAddress1.setConsignee(receiveAddress.getConsignee());}
        if(receiveAddress.getPhoneNumber()!=null){receiveAddress1.setPhoneNumber(receiveAddress.getPhoneNumber());}
        if(receiveAddress.getAddress()!=null){receiveAddress1.setAddress(receiveAddress.getAddress());}
        receiveAddressMapper.updateById(receiveAddress1);

    }

    @Override
    public ReceiveAddress queryReceiveAddressByReceiveAddressId(Integer receiveAddressId){

        ReceiveAddress receiveAddress=receiveAddressMapper.selectById(receiveAddressId);
        return receiveAddress;
    }

    @Override
    public List<ReceiveAddress> queryReceiveAddressByCustomerId(Integer customerId){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id", customerId);
        List<ReceiveAddress>receiveAddress_list =receiveAddressMapper.selectList(queryWrapper);
        return  receiveAddress_list;
    }

    @Override
    public void deleteReceiveAddressById(Integer receiveAddressId){
        receiveAddressMapper.deleteById(receiveAddressId);
    }

    @Override
    @Transactional
    public void updateDefaultAddressById(Integer customerId,Integer receiveAddressId,Integer defaultAddress){

        if(defaultAddress==0){
            ReceiveAddress receiveAddress =receiveAddressMapper.selectById(receiveAddressId);
            receiveAddress.setDefaultAddress(0);
            receiveAddressMapper.updateById(receiveAddress);
        }
        else if(defaultAddress==1) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("customer_id", customerId);
            queryWrapper.eq("default_address", 1);

            ReceiveAddress old_default_address = receiveAddressMapper.selectOne(queryWrapper);
            if(old_default_address!=null)
            {
                old_default_address.setDefaultAddress(0);
                receiveAddressMapper.updateById(old_default_address);

                ReceiveAddress receiveAddress = receiveAddressMapper.selectById(receiveAddressId);
                receiveAddress.setDefaultAddress(1);
                receiveAddressMapper.updateById(receiveAddress);
            }
            else {
                ReceiveAddress receiveAddress = receiveAddressMapper.selectById(receiveAddressId);
                receiveAddress.setDefaultAddress(1);
                receiveAddressMapper.updateById(receiveAddress);
            }
        }


    }
}
