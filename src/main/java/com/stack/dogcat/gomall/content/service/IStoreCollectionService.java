package com.stack.dogcat.gomall.content.service;

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
    List<StoreCollectionResponseVo> listStoreCollection(Integer customerId);
    void deleteStoreCollection(Integer storeCollectionId);
}
