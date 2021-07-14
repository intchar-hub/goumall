package com.stack.dogcat.gomall.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.message.entity.Reply;
import com.stack.dogcat.gomall.message.mapper.ReplyMapper;
import com.stack.dogcat.gomall.message.requestVo.ReplySaveRequestVo;
import com.stack.dogcat.gomall.message.service.IReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements IReplyService {

    @Autowired
    ReplyMapper replyMapper;

    @Override
    public void saveStoreReply(ReplySaveRequestVo requestVo) {
        Reply exitReply = replyMapper.selectOne(new QueryWrapper<Reply>().eq("comment_id",requestVo.getCommentId()));
        if(exitReply!=null){
            throw new RuntimeException("你已经回复过了");
        }
        else {
            Reply reply=new Reply();
            reply.setCommentId(requestVo.getCommentId());
            reply.setContent(requestVo.getContent());
            reply.setGmtCreate(LocalDateTime.now());
            int i = replyMapper.insert(reply);
            if(i==0){
                throw new RuntimeException("回复上传失败");
            }
        }
    }
}
