package com.stack.dogcat.gomall.message.service;

import com.stack.dogcat.gomall.message.entity.ChatUserLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.message.responseVo.ChatUserLinkResponseVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IChatUserLinkService extends IService<ChatUserLink> {

    /**
     * 查询聊天双方的关联id
     * @return
     */
    Integer selectAssociation(Integer customerId, Integer storeId);

    /**
     * 是否第一次聊天
     * @return
     */
    ChatUserLinkResponseVo CheckFirstChat(Integer customerId, Integer storeId);
}
