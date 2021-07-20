package com.stack.dogcat.gomall.message.service;

import com.stack.dogcat.gomall.message.entity.ChatList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.message.responseVo.CustomerChatListResponseVo;
import com.stack.dogcat.gomall.message.responseVo.StoreChatListResponseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IChatListService extends IService<ChatList> {

    List<CustomerChatListResponseVo> getCustomerChatList(Integer customerId);
    List<StoreChatListResponseVo> getStoreChatList(Integer storeId);
    void updateWindow(Integer chatUserLinkId, Integer senderType);
    void resetWindow(Integer chatUserLinkId, Integer senderType);

}
