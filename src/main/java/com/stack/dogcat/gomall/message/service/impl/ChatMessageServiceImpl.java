package com.stack.dogcat.gomall.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.message.entity.ChatList;
import com.stack.dogcat.gomall.message.entity.ChatMessage;
import com.stack.dogcat.gomall.message.mapper.ChatListMapper;
import com.stack.dogcat.gomall.message.mapper.ChatMessageMapper;
import com.stack.dogcat.gomall.message.mapper.ChatUserLinkMapper;
import com.stack.dogcat.gomall.message.requestVo.MessageSaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.MessageResponseVo;
import com.stack.dogcat.gomall.message.service.IChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements IChatMessageService {

    @Autowired
    ChatMessageMapper chatMessageMapper;

    @Autowired
    ChatListMapper chatListMapper;

    @Autowired
    ChatUserLinkMapper chatUserLinkMapper;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(MessageSaveRequestVo chatMessageVo) {
        //获取message
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setChatUserLinkId(chatMessageVo.getChatUserLinkId());
        chatMessage.setSenderType(chatMessageVo.getSenderType());
        chatMessage.setContent(chatMessageVo.getContent());
        chatMessage.setGmtCreate(LocalDateTime.now());
        chatMessage.setLatest(1);

        //将该连接的所有message设为不是最后一条消息
        UpdateWrapper<ChatMessage> wrapper=new UpdateWrapper<>();
        wrapper.eq("chat_user_link_id",chatMessage.getChatUserLinkId()).eq("latest",1).set("latest",0);
        chatMessageMapper.update(null,wrapper);

        //查看是否在同一窗口,更新未读数
        int senderType=1-chatMessage.getSenderType();
        ChatList chatList = chatListMapper.selectOne(new QueryWrapper<ChatList>().eq("chat_user_link_id",chatMessage.getChatUserLinkId()).eq("sender_type",senderType));
        int i = chatList.getCustomerWindow()+chatList.getStoreWindow();
        if(i==1){
            chatList.setUnreadNum(chatList.getUnreadNum()+1);
        }
        else if(i==2){
            chatList.setUnreadNum(0);
        }
        chatListMapper.updateById(chatList);

        chatMessageMapper.insert(chatMessage);
    }

    @Override
    public Integer getToUser(Integer chatUserLinkId, Integer senderType) {
        if(senderType==0){
            return chatUserLinkMapper.selectById(chatUserLinkId).getStoreId();
        }
        else if(senderType==1){
            return chatUserLinkMapper.selectById(chatUserLinkId).getCustomerId();
        }
        else{
            return null;
        }
    }

    @Override
    public Map<String, Object> getOneMessage(MessageSaveRequestVo chatMessageVo) {
        Map<String, Object> resultMap = new HashMap<>();
        Integer senderType = chatMessageVo.getSenderType();
        if(senderType==0){
            String senderName= customerMapper.selectById(chatUserLinkMapper.selectById(chatMessageVo.getChatUserLinkId()).getCustomerId()).getUserName();
            resultMap.put("sendUser", senderName);
            resultMap.put("content",chatMessageVo.getContent());
            return resultMap;
        }
        else if(senderType==1){
            String senderName= storeMapper.selectById(chatUserLinkMapper.selectById(chatMessageVo.getChatUserLinkId()).getStoreId()).getStoreName();
            resultMap.put("sendUser", senderName);
            resultMap.put("content",chatMessageVo.getContent());
            return resultMap;
        }
        else{
            return null;
        }

    }

    @Override
    public PageResponseVo<MessageResponseVo> getMessages(Integer chatUserLinkId, Integer senderType, Integer pageNum, Integer pageSize) {
        Page<ChatMessage> page = new Page<>(pageNum,pageSize);
        IPage<ChatMessage> chatMessagePage= chatMessageMapper.selectPage(page,new QueryWrapper<ChatMessage>().eq("chat_user_link_id",chatUserLinkId).orderByDesc("id"));
        PageResponseVo<MessageResponseVo> responseVoPage= new PageResponseVo(chatMessagePage);
        List<ChatMessage> chatMessages = chatMessagePage.getRecords();
        List<MessageResponseVo> responseVos = new ArrayList<>();
        System.out.println(chatUserLinkId+"这是获得的斤斤计较急急急急急急急急急急急急急急急");
        System.out.println(chatUserLinkId);
        System.out.println(chatUserLinkMapper.selectById(chatUserLinkId));
        System.out.println(chatUserLinkMapper.selectById(chatUserLinkId).getStoreId());
        Store store = storeMapper.selectById(chatUserLinkMapper.selectById(chatUserLinkId).getStoreId());
        Customer customer = customerMapper.selectById(chatUserLinkMapper.selectById(chatUserLinkId).getCustomerId());
        //获取数据
        for (ChatMessage chatMessage:chatMessages) {
            MessageResponseVo responseVo = new MessageResponseVo();
            if(chatMessage.getSenderType()==1){
                responseVo.setReceiverName(store.getStoreName());
                responseVo.setReceiverAvatarPath(store.getAvatarPath());
                responseVo.setSenderType(1);
            }
            else {
                responseVo.setReceiverName(customer.getUserName());
                responseVo.setReceiverAvatarPath(customer.getAvatorPath());
                responseVo.setSenderType(0);
            }
            responseVo.setMessage(chatMessage.getContent());
            responseVo.setGmtCreate(chatMessage.getGmtCreate());
            responseVos.add(responseVo);
        }

        Collections.sort(responseVos);
        responseVoPage.setData(responseVos);
        return responseVoPage;
    }
}
