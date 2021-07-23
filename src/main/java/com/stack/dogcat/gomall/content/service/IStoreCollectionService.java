package com.stack.dogcat.gomall.content.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.entity.StoreCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.content.responseVo.StoreCollectionResponseVo;

import java.util.List;

/**
 * <p>
 * 商店收藏 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IStoreCollectionService extends IService<StoreCollection> {

    void saveStoreCollection(Integer customerId,Integer storeId);
    PageResponseVo<StoreCollectionResponseVo> listStoreCollection(Integer customerId,Integer pageNum,Integer pageSize);
    void deleteStoreCollection(Integer storeCollectionId);
    void switchStoreCollection (Integer customerId, Integer storeId,Integer status);
}
