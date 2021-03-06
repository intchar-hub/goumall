package com.stack.dogcat.gomall.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.message.entity.ChatList;
import com.stack.dogcat.gomall.message.entity.ChatMessage;
import com.stack.dogcat.gomall.message.entity.ChatUserLink;
import com.stack.dogcat.gomall.message.mapper.ChatListMapper;
import com.stack.dogcat.gomall.message.mapper.ChatMessageMapper;
import com.stack.dogcat.gomall.message.mapper.ChatUserLinkMapper;
import com.stack.dogcat.gomall.message.responseVo.ChatUserLinkResponseVo;
import com.stack.dogcat.gomall.message.service.IChatUserLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ChatUserLinkServiceImpl extends ServiceImpl<ChatUserLinkMapper, ChatUserLink> implements IChatUserLinkService {

    @Autowired
    ChatUserLinkMapper chatUserLinkMapper;

    @Autowired
    ChatListMapper chatListMapper;

    @Autowired
    ChatMessageMapper chatMessageMapper;

    @Autowired
    StoreMapper storeMapper;

    @Override
    public Integer selectAssociation(Integer customerId, Integer storeId) {
        //查询连接id
        ChatUserLink link = chatUserLinkMapper.selectOne(new QueryWrapper<ChatUserLink>().eq("customer_id",customerId).eq("store_id",storeId));
        if(link==null){
            return -1;
        }
        else{
            return link.getId();
        }
    }

    /**
     * 判断是否为第一次连接，如果是那么初始化连接
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatUserLinkResponseVo CheckFirstChat(Integer customerId, Integer storeId) {
        //判断是否为第一次连接
        Integer linkId = this.selectAssociation(customerId,storeId);
        ChatUserLinkResponseVo responseVo = new ChatUserLinkResponseVo();
        Store store = storeMapper.selectById(storeId);
        responseVo.setStoreName(store.getStoreName());
        responseVo.setStoreAvatar(store.getAvatarPath());
        if(linkId==-1){
            //添加连接
            ChatUserLink chatUserLink=new ChatUserLink();
            chatUserLink.setCustomerId(customerId);
            chatUserLink.setStoreId(storeId);
            chatUserLink.setGmtCreate(LocalDateTime.now());
            chatUserLinkMapper.insert(chatUserLink);

            chatUserLink = chatUserLinkMapper.selectOne(new QueryWrapper<ChatUserLink>().eq("customer_id",customerId).eq("store_id",storeId));

            //添加两条聊天列表数据（商家，消费者）
            ChatList storeChatList = new ChatList();
            ChatList customerChatList = new ChatList();
            storeChatList.setChatUserLinkId(chatUserLink.getId());
            storeChatList.setSenderType(1);
            storeChatList.setUnreadNum(0);
            storeChatList.setCustomerWindow(0);
            storeChatList.setStoreWindow(0);
            customerChatList.setChatUserLinkId(chatUserLink.getId());
            customerChatList.setSenderType(0);
            customerChatList.setUnreadNum(0);
            customerChatList.setCustomerWindow(0);
            customerChatList.setStoreWindow(0);
            chatListMapper.insert(storeChatList);
            chatListMapper.insert(customerChatList);

            //插入一条空白消息（为了更好地联表查询数据）
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setChatUserLinkId(chatUserLink.getId());
            chatMessage.setContent("");
            chatMessage.setSenderType(0);
            chatMessage.setGmtCreate(LocalDateTime.now());
            chatMessage.setLatest(1);
            chatMessageMapper.insert(chatMessage);

            responseVo.setChatUserLink(chatUserLinkMapper.selectOne(new QueryWrapper<ChatUserLink>().eq("customer_id",customerId).eq("store_id",storeId)).getId());
        }
        else{
            responseVo.setChatUserLink(chatUserLinkMapper.selectOne(new QueryWrapper<ChatUserLink>().eq("customer_id",customerId).eq("store_id",storeId)).getId());
        }
        return responseVo;
    }
}
