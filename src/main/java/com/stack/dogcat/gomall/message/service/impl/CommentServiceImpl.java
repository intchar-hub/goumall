package com.stack.dogcat.gomall.message.service.impl;

import com.stack.dogcat.gomall.message.entity.Comment;
import com.stack.dogcat.gomall.message.mapper.CommentMapper;
import com.stack.dogcat.gomall.message.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
