package com.stack.dogcat.gomall.message.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.message.entity.ChatMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.message.requestVo.MessageSaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.MessageResponseVo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IChatMessageService extends IService<ChatMessage> {

    /**
     * 保存聊天记录
     */
    void saveMessage(MessageSaveRequestVo chatMessageVo);

    /**
     * 查找收信人
     */
    Integer getToUser(Integer chatUserLinkId,Integer senderType);

    /**
     * 获取一条消息
     */
    Map<String, Object> getOneMessage(MessageSaveRequestVo chatMessageVo);

    /**
     * 获取聊天记录
     */
    PageResponseVo<MessageResponseVo> getMessages(Integer chatUserLinkId, Integer senderType, Integer pageNum, Integer pageSize);

}