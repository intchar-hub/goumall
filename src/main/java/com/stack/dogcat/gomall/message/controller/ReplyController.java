package com.stack.dogcat.gomall.message.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.message.requestVo.ReplySaveRequestVo;
import com.stack.dogcat.gomall.message.service.IReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/mms/reply")
public class ReplyController {

    @Autowired
    IReplyService replyService;

    /**
     * 商家对评论回复
     */
    @PostMapping("/saveStoreReply")
    public SysResult saveStoreReply(@Valid @RequestBody ReplySaveRequestVo replySaveRequestVo){
        try{
            replyService.saveStoreReply(replySaveRequestVo);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

}
