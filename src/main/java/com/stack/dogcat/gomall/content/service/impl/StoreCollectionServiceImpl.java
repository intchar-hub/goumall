package com.stack.dogcat.gomall.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.content.entity.ProductCollection;
import com.stack.dogcat.gomall.content.entity.StoreCollection;
import com.stack.dogcat.gomall.content.mapper.StoreCollectionMapper;
import com.stack.dogcat.gomall.content.responseVo.ProductCollectionResponseVo;
import com.stack.dogcat.gomall.content.responseVo.StoreCollectionResponseVo;
import com.stack.dogcat.gomall.content.service.IStoreCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商店收藏 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class StoreCollectionServiceImpl extends ServiceImpl<StoreCollectionMapper, StoreCollection> implements IStoreCollectionService {

    @Autowired
    StoreCollectionMapper storeCollectionMapper;

    @Autowired
    StoreMapper storeMapper;

    @Override
    public void saveStoreCollection(Integer customerId, Integer storeId) {
        StoreCollection storeCollection=new StoreCollection();
        storeCollection.setCustomerId(customerId);
        storeCollection.setStoreId(storeId);
        storeCollection.setGmtCreate(LocalDateTime.now());
        //插入数据库
        storeCollectionMapper.insert(storeCollection);
    }

    @Override
    public List<StoreCollectionResponseVo> listStoreCollection(Integer customerId) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("customer_id",customerId);
        List<StoreCollection> collections=storeCollectionMapper.selectList(wrapper);
        List<StoreCollectionResponseVo> collectionResponseVos = new ArrayList<>();

        for (StoreCollection storeCollection:collections) {
            StoreCollectionResponseVo responseVo= new StoreCollectionResponseVo();
            responseVo.setStoreCollectionId(storeCollection.getId());
            responseVo.setStoreId(storeCollection.getStoreId());
            responseVo.setStoreName(storeMapper.selectById(storeCollection.getStoreId()).getStoreName());
            responseVo.setAvatarPath(storeMapper.selectById(storeCollection.getStoreId()).getAvatarPath());
            responseVo.setDescription(storeMapper.selectById(storeCollection.getStoreId()).getDescription());
            collectionResponseVos.add(responseVo);
        }
        return collectionResponseVos;
    }

    @Override
    public void deleteStoreCollection(Integer storeCollectionId) {
        StoreCollection storeCollection = storeCollectionMapper.selectById(storeCollectionId);
        if(storeCollection==null){
            throw new RuntimeException("该收藏已取消");
        }
        storeCollectionMapper.deleteById(storeCollectionId);
    }
}
