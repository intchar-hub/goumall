package com.stack.dogcat.gomall.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.message.entity.Comment;
import com.stack.dogcat.gomall.message.entity.Reply;
import com.stack.dogcat.gomall.message.mapper.CommentMapper;
import com.stack.dogcat.gomall.message.mapper.ReplyMapper;
import com.stack.dogcat.gomall.message.requestVo.CommentSaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.CommentByStoreResponseVo;
import com.stack.dogcat.gomall.message.responseVo.CommentResponse;
import com.stack.dogcat.gomall.message.responseVo.CommentResponseVo;
import com.stack.dogcat.gomall.message.responseVo.ReplyResponse;
import com.stack.dogcat.gomall.message.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    ReplyMapper replyMapper;

    /**
     * 顾客发表评论
     */
    @Override
    public void saveComment(CommentSaveRequestVo commentSaveRequestVo) {
        Comment comment=new Comment();
        comment.setCustomerId(commentSaveRequestVo.getCustomerId());
        comment.setProductId(commentSaveRequestVo.getProductId());
        comment.setContent(commentSaveRequestVo.getContent());
        comment.setLevel(commentSaveRequestVo.getLevel());
        comment.setGmtCreate(LocalDateTime.now());

        Product product = productMapper.selectById(commentSaveRequestVo.getProductId());
        if(product==null){
            throw new RuntimeException("查找关联店铺失败");
        }
        comment.setStoreId(product.getStoreId());

        int i = commentMapper.insert(comment);
        if(i==0){
            throw new RuntimeException("评论上传失败");
        }
    }

    @Override
    public List<CommentResponseVo> listCommentByProduct(Integer productId) {

        List<Comment> comments=commentMapper.selectList(new QueryWrapper<Comment>().eq("product_id",productId));
        List<CommentResponseVo> commentResponseVos=new ArrayList<>();

        for (Comment comment:comments) {
            CommentResponseVo commentResponseVo=new CommentResponseVo();
            CommentResponse commentResponse=new CommentResponse();
            Customer customer = customerMapper.selectById(comment.getCustomerId());
            if(customer==null){
                throw new RuntimeException("找不到评论的顾客");
            }
            commentResponse.setCustomerName(customer.getUserName());
            commentResponse.setAvatarPath(customer.getAvatorPath());
            commentResponse.setContent(comment.getContent());
            commentResponse.setLevel(comment.getLevel());
            commentResponse.setGmtCreate(comment.getGmtCreate());
            commentResponseVo.setCustomer(commentResponse);

            ReplyResponse replyResponseVo =new ReplyResponse();
            Reply reply = replyMapper.selectOne(new QueryWrapper<Reply>().eq("comment_id",comment.getId()));
            replyResponseVo.setContent(reply.getContent());
            replyResponseVo.setGmtCreate(reply.getGmtCreate());
            commentResponseVo.setStore(replyResponseVo);

            commentResponseVos.add(commentResponseVo);
        }

        return commentResponseVos;
    }

    @Override
    public PageResponseVo<CommentByStoreResponseVo> listCommentByStore(Integer storeId) {
        return null;
    }
}
