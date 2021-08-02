package com.stack.dogcat.gomall.message.controller;


import com.stack.dogcat.gomall.annotation.CurrentUser;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.message.responseVo.ChatUserLinkResponseVo;
import com.stack.dogcat.gomall.message.service.IChatUserLinkService;
import com.stack.dogcat.gomall.user.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/mms/chat-user-link")
public class ChatUserLinkController {

    @Autowired
    IChatUserLinkService chatUserLinkService;

    @PostMapping("/firstChat")
    public SysResult firstChat(Integer customerId, Integer storeId){
        try{
            ChatUserLinkResponseVo responseVo =chatUserLinkService.CheckFirstChat(customerId,storeId);
            //不是第一次建立连接
            return SysResult.success(responseVo);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知错误");
        }
    }

}
