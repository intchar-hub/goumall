package com.stack.dogcat.gomall.content.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.content.entity.ProductCollection;
import com.stack.dogcat.gomall.content.responseVo.ProductCollectionResponseVo;
import com.stack.dogcat.gomall.content.service.IProductCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    IProductCollectionService productCollectionService;

    @PostMapping("/saveProductCollection")
    public SysResult saveProductCollection(Integer customerId,Integer productId){
        SysResult result=null;
        try{
            productCollectionService.saveProductCollection(customerId,productId);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    @GetMapping("/listProductCollection")
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

}
