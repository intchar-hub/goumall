package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.entity.AttributeCollection;
import com.stack.dogcat.gomall.product.responseVo.AttributeCollectionResopnseVo;
import com.stack.dogcat.gomall.product.responseVo.StoreAttributesListResponseVo;
import com.stack.dogcat.gomall.product.service.IAttributeCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商家自定义的属性集合 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/product/attribute-collection")
public class AttributeCollectionController {

    @Autowired
    IAttributeCollectionService attributeCollectionService;

    @PostMapping("/saveAttributeCollection")
    public SysResult saveAttributeCollection(Integer storeId ,String name ){
        try {
            attributeCollectionService.saveAttributeCollection(storeId,name);
            return SysResult.success();
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
    }

    @GetMapping("/listAttributeCollections")
    public SysResult listAttributeCollections(Integer storeId,Integer pageNum,Integer pageSize){
        try {
            PageResponseVo<AttributeCollectionResopnseVo> reponseVos = attributeCollectionService.listAttributeCollections(storeId,pageNum,pageSize);
            return SysResult.success(reponseVos);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("列举失败");
        }
    }

    @GetMapping("/listAttributesByStore")
    public SysResult listAttributesByStore(Integer storeId){
        try{
            List<StoreAttributesListResponseVo> responseVoList = attributeCollectionService.listAttributesByStore(storeId);
            return SysResult.success(responseVoList);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
    }


}
