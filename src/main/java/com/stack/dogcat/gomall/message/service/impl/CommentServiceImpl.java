package com.stack.dogcat.gomall.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.message.entity.Comment;
import com.stack.dogcat.gomall.message.entity.Reply;
import com.stack.dogcat.gomall.message.mapper.CommentMapper;
import com.stack.dogcat.gomall.message.mapper.ReplyMapper;
import com.stack.dogcat.gomall.message.requestVo.CommentSaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.CommentQueryByStoreResponseVo;
import com.stack.dogcat.gomall.message.responseVo.CommentResponse;
import com.stack.dogcat.gomall.message.responseVo.CommentResponseVo;
import com.stack.dogcat.gomall.message.responseVo.ReplyResponse;
import com.stack.dogcat.gomall.message.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    OrderMapper orderMapper;

    /**
     * 顾客发表评论
     */
    @Override
    @Transactional
    @Token.UserLoginToken
    public void saveComment(CommentSaveRequestVo commentSaveRequestVo) {
        Comment comment=new Comment();
        comment.setCustomerId(commentSaveRequestVo.getCustomerId());
        comment.setProductId(commentSaveRequestVo.getProductId());
        comment.setOrderId(commentSaveRequestVo.getOrderId());
        comment.setContent(commentSaveRequestVo.getContent());
        comment.setLevel(commentSaveRequestVo.getLevel());
        comment.setGmtCreate(LocalDateTime.now());

        Product product = productMapper.selectById(commentSaveRequestVo.getProductId());
        if(product==null){
            throw new RuntimeException("查找关联店铺失败");
        }
        comment.setStoreId(product.getStoreId());

        Order order = orderMapper.selectById(comment.getOrderId());
        order.setStatus(6);
        int j=orderMapper.updateById(order);
        if(j==0){
            throw new RuntimeException("评论上传失败");
        }

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
            commentResponse.setCommentId(comment.getId());
            commentResponse.setCustomerName(customer.getUserName());
            commentResponse.setAvatarPath(customer.getAvatorPath());
            commentResponse.setContent(comment.getContent());
            commentResponse.setLevel(comment.getLevel());
            commentResponse.setGmtCreate(comment.getGmtCreate());
            commentResponse.setOrderNumber(orderMapper.selectById(comment.getOrderId()).getOrderNumber());
            commentResponseVo.setCustomer(commentResponse);

            ReplyResponse replyResponseVo =new ReplyResponse();
            Reply reply = replyMapper.selectOne(new QueryWrapper<Reply>().eq("comment_id",comment.getId()));
            if(reply!=null){
                replyResponseVo.setContent(reply.getContent());
                replyResponseVo.setGmtCreate(reply.getGmtCreate());
                commentResponseVo.setStore(replyResponseVo);
            }
            else{
                commentResponseVo.setStore(replyResponseVo);
            }

            commentResponseVos.add(commentResponseVo);
        }

        return commentResponseVos;
    }

    @Override
    public PageResponseVo<CommentQueryByStoreResponseVo> listCommentByStore(Integer storeId, Integer pageNum, Integer pageSize) {
        Page<Product> page = new Page<>(pageNum,pageSize);
        IPage<Product> productPage = productMapper.selectPage(page,new QueryWrapper<Product>().eq("store_id",storeId).eq("status",1));
        List<Product> products=productPage.getRecords();
        List<CommentQueryByStoreResponseVo> responseVos = new ArrayList<>();
        for (Product product:products) {
            CommentQueryByStoreResponseVo responseVo = new CommentQueryByStoreResponseVo();
            responseVo.setProductId(product.getId());
            responseVo.setProductName(product.getName());
            responseVo.setImagePath(product.getImagePath());
            responseVo.setGmtCreate(product.getGmtCreate());
            responseVo.setComments(this.listCommentByProduct(product.getId()));
            responseVos.add(responseVo);
        }
        PageResponseVo<CommentQueryByStoreResponseVo> responseVoPage = new PageResponseVo(productPage);
        responseVoPage.setData(responseVos);
        return responseVoPage;
    }
}
