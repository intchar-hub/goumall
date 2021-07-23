package com.stack.dogcat.gomall.message.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.message.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.message.requestVo.CommentSaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.CommentResponseVo;
import com.stack.dogcat.gomall.message.responseVo.CommentByStoreResponseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface ICommentService extends IService<Comment> {

    void saveComment(CommentSaveRequestVo commentSaveRequestVo);
    List<CommentResponseVo> listCommentByProduct(Integer productId);
    PageResponseVo<CommentByStoreResponseVo> listCommentByStore(Integer storeId);
}
