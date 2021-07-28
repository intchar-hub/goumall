package com.stack.dogcat.gomall.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.stack.dogcat.gomall.message.entity.ChatList;
import com.stack.dogcat.gomall.message.entity.ChatMessage;
import com.stack.dogcat.gomall.message.entity.ChatUserLink;
import com.stack.dogcat.gomall.message.mapper.ChatListMapper;
import com.stack.dogcat.gomall.message.mapper.ChatMessageMapper;
import com.stack.dogcat.gomall.message.mapper.ChatUserLinkMapper;
import com.stack.dogcat.gomall.message.responseVo.CustomerChatListResponseVo;
import com.stack.dogcat.gomall.message.responseVo.StoreChatListResponseVo;
import com.stack.dogcat.gomall.message.service.IChatListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ChatListServiceImpl extends ServiceImpl<ChatListMapper, ChatList> implements IChatListService {

    @Autowired
    ChatListMapper chatListMapper;

    @Autowired
    ChatUserLinkMapper chatUserLinkMapper;

    @Autowired
    ChatMessageMapper chatMessageMapper;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    CustomerMapper customerMapper;

    /**
     * 获取顾客消息列表
     */
    @Override
    public List<CustomerChatListResponseVo> getCustomerChatList(Integer customerId) {
        List<ChatUserLink> chatUserLinks=chatUserLinkMapper.selectList(new QueryWrapper<ChatUserLink>().eq("customer_id",customerId));
        List<ChatList> chatLists = new ArrayList<>();
        for (ChatUserLink chatUserLink:chatUserLinks) {
            ChatList chatList = chatListMapper.selectOne(new QueryWrapper<ChatList>().eq("chat_user_link_id",chatUserLink.getId()).eq("sender_type",0));
            chatLists.add(chatList);
        }
        List<CustomerChatListResponseVo> responseVos = new ArrayList<>();
        for (ChatList chatList:chatLists) {
            CustomerChatListResponseVo responseVo = new CustomerChatListResponseVo();
            Store store = storeMapper.selectById(chatUserLinkMapper.selectById(chatList.getChatUserLinkId()).getStoreId());
            ChatMessage chatMessage = chatMessageMapper.selectOne(new QueryWrapper<ChatMessage>().eq("chat_user_link_id",chatList.getChatUserLinkId()).eq("latest",1));
            responseVo.setChatUserLinkId(chatList.getChatUserLinkId());
            responseVo.setUnreadNum(chatList.getUnreadNum());
            responseVo.setStoreName(store.getStoreName());
            responseVo.setStoreAvatorPath(store.getAvatarPath());
            responseVo.setLatestMessage(chatMessage.getContent());
            responseVo.setLatestGmtCreate(chatMessage.getGmtCreate());
            responseVos.add(responseVo);
        }
        return responseVos;
    }

    /**
     * 获取店铺消息列表
     */
    @Override
    public List<StoreChatListResponseVo> getStoreChatList(Integer storeId) {
        List<ChatUserLink> chatUserLinks=chatUserLinkMapper.selectList(new QueryWrapper<ChatUserLink>().eq("store_id",storeId));
        List<ChatList> chatLists = new ArrayList<>();
        for (ChatUserLink chatUserLink:chatUserLinks) {
            ChatList chatList = chatListMapper.selectOne(new QueryWrapper<ChatList>().eq("chat_user_link_id",chatUserLink.getId()).eq("sender_type",1));
            chatLists.add(chatList);
        }
        List<StoreChatListResponseVo> responseVos = new ArrayList<>();
        for (ChatList chatList:chatLists) {
            StoreChatListResponseVo responseVo = new StoreChatListResponseVo();
            Customer customer = customerMapper.selectById(chatUserLinkMapper.selectById(chatList.getChatUserLinkId()).getCustomerId());
            ChatMessage chatMessage = chatMessageMapper.selectOne(new QueryWrapper<ChatMessage>().eq("chat_user_link_id",chatList.getChatUserLinkId()).eq("latest",1));
            responseVo.setChatUserLinkId(chatList.getChatUserLinkId());
            responseVo.setUnreadNum(chatList.getUnreadNum());
            responseVo.setCustomerName(customer.getUserName());
            responseVo.setCustomerAvatorPath(customer.getAvatorPath());
            responseVo.setLatestMessage(chatMessage.getContent());
            responseVo.setLatestGmtCreate(chatMessage.getGmtCreate());
            responseVos.add(responseVo);
        }
        return responseVos;
    }

    /**
     * 进入窗口后更新在线情况
     */
    @Override
    public void updateWindow(Integer chatUserLinkId, Integer senderType) {
        //顾客方更新
        if(senderType==0){
            //将顾客窗口和商家关联窗口置为在线
            UpdateWrapper<ChatList> wrapper_1 = new UpdateWrapper<ChatList>();
            wrapper_1.eq("chat_user_link_id",chatUserLinkId).set("customer_window",1);
            chatListMapper.update(null,wrapper_1);

            //清除未读数
            UpdateWrapper<ChatList> wrapper_2 = new UpdateWrapper<ChatList>();
            wrapper_2.eq("chat_user_link_id",chatUserLinkId).eq("sender_type",0).set("unread_num",0);
            chatListMapper.update(null,wrapper_2);

            //将顾客其余窗口和与其余商家窗口置为不在线
            UpdateWrapper<ChatList> wrapper_3 = new UpdateWrapper<ChatList>();
            wrapper_3.ne("chat_user_link_id",chatUserLinkId).set("customer_window",0);
            chatListMapper.update(null,wrapper_3);
        }
        //商家方更新
        else if(senderType==1){
            //将商家窗口和顾客关联窗口置为在线
            UpdateWrapper<ChatList> wrapper_1 = new UpdateWrapper<ChatList>();
            wrapper_1.eq("chat_user_link_id",chatUserLinkId).set("store_window",1);
            chatListMapper.update(null,wrapper_1);

            //清除未读数
            UpdateWrapper<ChatList> wrapper_2 = new UpdateWrapper<ChatList>();
            wrapper_2.eq("chat_user_link_id",chatUserLinkId).eq("sender_type",1).set("unread_num",0);
            chatListMapper.update(null,wrapper_2);

            //将商家其余窗口和与其余顾客窗口置为不在线
            UpdateWrapper<ChatList> wrapper_3 = new UpdateWrapper<ChatList>();
            wrapper_3.ne("chat_user_link_id",chatUserLinkId).set("store_window",0);
            chatListMapper.update(null,wrapper_3);
        }
    }

    /**
     * 离开所有窗口更新在线情况
     */
    @Override
    public void resetWindow(Integer chatUserLinkId, Integer senderType) {
        //顾客方更新
        if(senderType==0){
            UpdateWrapper<ChatList> wrapper = new UpdateWrapper<ChatList>();
            wrapper.eq("chat_user_link_id",chatUserLinkId).set("customer_window",0);
            chatListMapper.update(null,wrapper);
        }
        //商家方更新
        else if(senderType==1){
            UpdateWrapper<ChatList> wrapper = new UpdateWrapper<ChatList>();
            wrapper.eq("chat_user_link_id",chatUserLinkId).set("store_window",0);
            chatListMapper.update(null,wrapper);
        }
    }
}
