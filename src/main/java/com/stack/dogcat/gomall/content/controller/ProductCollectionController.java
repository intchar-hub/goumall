package com.stack.dogcat.gomall.content.controller;


import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.content.entity.ProductCollection;
import com.stack.dogcat.gomall.content.responseVo.ProductCollectionResponseVo;
import com.stack.dogcat.gomall.content.service.IProductCollectionService;
import com.stack.dogcat.gomall.product.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品收藏 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/cms/product-collection")
public class ProductCollectionController {

    private static Logger logger = LoggerFactory.getLogger(ProductCollectionController.class);

    @Autowired
    IProductCollectionService productCollectionService;

    @PostMapping("/saveProductCollection")
    @Token.UserLoginToken
    public SysResult saveProductCollection(Integer customerId,Integer productId){
        SysResult result=null;
        try{
            productCollectionService.saveProductCollection(customerId,productId);
            logger.info("save_pro_col->current_customer.id:{},star_pro.id:{}",new Object[]{customerId.toString(), productId.toString()});
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    @GetMapping("/listProductCollection")
    @Token.UserLoginToken
    public SysResult listProductCollection(Integer customerId,Integer pageNum,Integer pageSize){
        PageResponseVo<ProductCollectionResponseVo> responseVos=null;
        try{
            responseVos = productCollectionService.listProductCollection(customerId,pageNum,pageSize);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知异常");
        }
        return SysResult.success(responseVos);
    }

    @DeleteMapping("/deleteProductCollection")
    @Token.UserLoginToken
    public SysResult deleteProductCollection(Integer productCollectionId){
        try{
            productCollectionService.deleteProductCollection(productCollectionId);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    @PostMapping("/switchProductCollection")
    @Token.UserLoginToken
    public SysResult switchProductCollection(Integer customerId,Integer productId,Integer status){
        try{
            productCollectionService.switchProductCollection(customerId,productId,status);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    @GetMapping("/checkProductCollection")
    @Token.UserLoginToken
    public SysResult checkProductCollection(Integer customerId, Integer productId){
        try{
            int i = productCollectionService.checkProductCollection(customerId,productId);
            Map<String,Integer> map = new HashMap<>();
            map.put("isCollected",i);
            return SysResult.success(map);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
    }


}
