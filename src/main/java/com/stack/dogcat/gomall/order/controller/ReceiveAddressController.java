package com.stack.dogcat.gomall.order.controller;


import com.stack.dogcat.gomall.annotation.CurrentUser;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.entity.ReceiveAddress;
import com.stack.dogcat.gomall.order.responseVo.ReceiveAddressQueryResponseVo;
import com.stack.dogcat.gomall.order.service.IOrderService;
import com.stack.dogcat.gomall.order.service.IReceiveAddressService;
import com.stack.dogcat.gomall.user.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 收货地址表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/order/receive-address")
public class ReceiveAddressController {

    @Autowired
    IReceiveAddressService receiveAddressService;


    /**
     * 用户添加收货地址
     * @param current_customer
     * @param address
     * @param phoneNumber
     * @return
     */
    @PostMapping("/saveReceiveAddress")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult saveReceiveAddress(@CurrentUser Customer current_customer, String consignee,String address,String phoneNumber) {
        try{
            Integer customerId =current_customer.getId();
            receiveAddressService.insertReceiveAddressByCustomerId(customerId,consignee,address,phoneNumber);
            SysResult result =SysResult.success();
            return  result;
        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
    }


    /**
     * 用户修改收货地址
     * @param receiveAddressId
     * @param address
     * @param phoneNumber
     * @return
     */
    @PostMapping("/updateReceiveAddress")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult updateReceiveAddress(Integer receiveAddressId,String consignee,String address,String phoneNumber){

        try{
            ReceiveAddress receiveAddress=receiveAddressService.queryReceiveAddressByReceiveAddressId(receiveAddressId);

            receiveAddress.setConsignee(consignee);
            receiveAddress.setAddress(address);
            receiveAddress.setPhoneNumber(phoneNumber);

            receiveAddressService.updateReceiveAddressById(receiveAddressId,receiveAddress);
            SysResult result =SysResult.success();
            return result;
        }catch (Exception e){
            SysResult result =SysResult.error(e.getMessage());
            return result;
        }
    }

    /**
     * 用户修改默认收货地址
     * @param current_customer
     * @param receiveAddressId
     * @param defaultAddress
     * @return
     */
    @PostMapping("/updateDefaultAddressById")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult updateDefaultAddressById(@CurrentUser Customer current_customer,Integer receiveAddressId,Integer defaultAddress){

        try{
            receiveAddressService.updateDefaultAddressById(current_customer.getId(),receiveAddressId,defaultAddress);

            SysResult result =SysResult.success();
            return result;
        }catch (Exception e){
            SysResult result =SysResult.error(e.getMessage());
            return result;
        }
    }

    /**
     * 用户查询收货地址
     * @param current_customer
     * @return
     */
    @GetMapping("/listReceiveAddress")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult queryListReceiveAddress(@CurrentUser Customer current_customer){
        try{
            Integer customerId = current_customer.getId();
            List<ReceiveAddress> receiveAddressQueryResponseVos=receiveAddressService.queryReceiveAddressByCustomerId(customerId);
            SysResult result = SysResult.success(receiveAddressQueryResponseVos);
            return result;
        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
    }


    /**
     * 用户删除收货地址
     * @param receiveAddressId
     * @return
     */
    @DeleteMapping("/deleteReceiveAddress")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult deleteReceiveAddress(Integer receiveAddressId){
        try{
            receiveAddressService.deleteReceiveAddressById(receiveAddressId);
            SysResult result = SysResult.success();
            return result;

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }

    }
}
