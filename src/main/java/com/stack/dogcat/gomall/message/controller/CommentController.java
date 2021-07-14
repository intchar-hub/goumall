package com.stack.dogcat.gomall.message.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.message.requestVo.CommentSaveRequestVo;
import com.stack.dogcat.gomall.message.requestVo.ReplySaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.CommentResponseVo;
import com.stack.dogcat.gomall.message.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author kxy
 * @since 2021/7/14
 */
@RestController
@RequestMapping("/mms/comment")
public class CommentController {

    @Autowired
    ICommentService commentService;


    /**
     * 消费者对订单（商品）评论
     */
    @PostMapping("/saveComment")
    public SysResult saveComment(@Valid @RequestBody CommentSaveRequestVo commentSaveRequestVo){
        try{
            commentService.saveComment(commentSaveRequestVo);
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
