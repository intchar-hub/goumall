package com.stack.dogcat.gomall.message.controller;


import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.content.controller.StoreCollectionController;
import com.stack.dogcat.gomall.message.requestVo.CommentSaveRequestVo;
import com.stack.dogcat.gomall.message.requestVo.ReplySaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.CommentResponseVo;
import com.stack.dogcat.gomall.message.service.ICommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author kxy
 * @since 2021/7/14
 */
@RestController
@CrossOrigin
@RequestMapping("/mms/comment")
public class CommentController {

    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    ICommentService commentService;


    /**
     * 消费者对订单（商品）评论
     */
    @PostMapping("/saveComment")
    @Token.UserLoginToken
    public SysResult saveComment(@Valid @RequestBody CommentSaveRequestVo commentSaveRequestVo){
        try{
            commentService.saveComment(commentSaveRequestVo);
            logger.info("comment->current_customer.id:{},comment_pro.id:{},comment_pro_level:{}",
                    new Object[]{commentSaveRequestVo.getCustomerId().toString(),
                            commentSaveRequestVo.getProductId().toString(),
                            commentSaveRequestVo.getLevel().toString()});
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    /**
     * 消费者查看所有评论和回复
     */
    @GetMapping("/listCommentByProduct")
    public  SysResult listCommentByProduct(Integer productId){
        try{
            List<CommentResponseVo> responseVos = commentService.listCommentByProduct(productId);
            return SysResult.success(responseVos);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
    }

}
