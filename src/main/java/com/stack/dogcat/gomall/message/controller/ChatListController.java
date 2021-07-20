package com.stack.dogcat.gomall.message.controller;


import com.stack.dogcat.gomall.annotation.CurrentUser;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.message.responseVo.CustomerChatListResponseVo;
import com.stack.dogcat.gomall.message.responseVo.StoreChatListResponseVo;
import com.stack.dogcat.gomall.message.service.IChatListService;
import com.stack.dogcat.gomall.user.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/mms/chat-list")
public class ChatListController {

    @Autowired
    IChatListService chatListService;

    @GetMapping("/getCustomerChatList")
    public SysResult getCustomerChatList(Integer customerId){
        try{
            List<CustomerChatListResponseVo> responseVos= chatListService.getCustomerChatList(customerId);
            return SysResult.success(responseVos);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知错误");
        }
    }

    @GetMapping("/getStoreChatList")
    public SysResult getStoreChatList(Integer storeId){
        try{
            List<StoreChatListResponseVo> responseVos= chatListService.getStoreChatList(storeId);
            return SysResult.success(responseVos);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知错误");
        }
    }

    @PostMapping("/updateWindow")
    public SysResult updateWindow(Integer chatUserLinkId, Integer senderType){
        try{
            chatListService.updateWindow(chatUserLinkId,senderType);
            return SysResult.success();
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知错误");
        }
    }

    @PostMapping("/resetWindow")
    public SysResult resetWindow(Integer chatUserLinkId, Integer senderType){
        try{
            chatListService.resetWindow(chatUserLinkId,senderType);
            return SysResult.success();
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知错误");
        }
    }
}
