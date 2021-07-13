package com.stack.dogcat.gomall.content.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.content.responseVo.StoreCollectionResponseVo;
import com.stack.dogcat.gomall.content.service.IStoreCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商店收藏 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/cms/store-collection")
public class StoreCollectionController {

    @Autowired
    IStoreCollectionService storeCollectionService;

    @PostMapping("/saveStoreCollection")
    public SysResult saveStoreCollection(Integer customerId, Integer storeId){
        SysResult result=null;
        try{
            storeCollectionService.saveStoreCollection(customerId,storeId);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    @GetMapping("/listStoreCollection")
    public SysResult listStoreCollection(Integer customerId){
        List<StoreCollectionResponseVo> responseVos=null;
        try{
            responseVos = storeCollectionService.listStoreCollection(customerId);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知异常");
        }
        return SysResult.success(responseVos);
    }

    @DeleteMapping("/deleteStoreCollection")
    public SysResult deleteStoreCollection(Integer storeCollectionId){
        try{
            storeCollectionService.deleteStoreCollection(storeCollectionId);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

}
