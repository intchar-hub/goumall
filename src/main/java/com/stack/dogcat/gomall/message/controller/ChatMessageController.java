package com.stack.dogcat.gomall.message.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.message.requestVo.MessageSaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.MessageResponseVo;
import com.stack.dogcat.gomall.message.service.IChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/mms/chat-message")
public class ChatMessageController {

    @Autowired
    IChatMessageService chatMessageService;

    @GetMapping("/getMessage")
    public SysResult getMessage(Integer chatUserLinkId, Integer senderType, Integer pageNum, Integer pageSize){
        try{
            PageResponseVo<MessageResponseVo> responseVos = chatMessageService.getMessages(chatUserLinkId, senderType,pageNum,pageSize);
            return SysResult.success(responseVos);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知错误");
        }
    }

}
