package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.AttributeCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.product.responseVo.AttributeCollectionResopnseVo;
import com.stack.dogcat.gomall.product.responseVo.StoreAttributesListResponseVo;

import java.util.List;

/**
 * @author kouxiaoyu
 * @since 2021-07-15
 */
public interface IAttributeCollectionService extends IService<AttributeCollection> {

    void saveAttributeCollection(Integer storeId ,String name );
    PageResponseVo<AttributeCollectionResopnseVo> listAttributeCollections(Integer storeId,Integer pageNum,Integer pageSize);
    List<StoreAttributesListResponseVo> listAttributesByStore(Integer storeId);
}
