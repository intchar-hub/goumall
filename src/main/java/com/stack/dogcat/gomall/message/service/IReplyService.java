package com.stack.dogcat.gomall.message.service;

import com.stack.dogcat.gomall.message.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.message.requestVo.ReplySaveRequestVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IReplyService extends IService<Reply> {

    void saveStoreReply(ReplySaveRequestVo requestVo);

}
