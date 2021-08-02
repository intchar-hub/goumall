package com.stack.dogcat.gomall.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.entity.ProductCollection;
import com.stack.dogcat.gomall.content.entity.StoreCollection;
import com.stack.dogcat.gomall.content.mapper.StoreCollectionMapper;
import com.stack.dogcat.gomall.content.responseVo.ProductCollectionResponseVo;
import com.stack.dogcat.gomall.content.responseVo.StoreCollectionResponseVo;
import com.stack.dogcat.gomall.content.service.IStoreCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.user.entity.Store;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    ProductMapper productMapper;

    @Override
    @Transactional
    public void saveStoreCollection(Integer customerId, Integer storeId) {
        StoreCollection storeCollection=new StoreCollection();
        storeCollection.setCustomerId(customerId);
        storeCollection.setStoreId(storeId);
        storeCollection.setGmtCreate(LocalDateTime.now());
        //插入数据库
        int i = storeCollectionMapper.insert(storeCollection);
        if(i==0){
            throw new RuntimeException("收藏失败");
        }
        Store store = storeMapper.selectById(storeId);
        store.setFansNum(store.getFansNum()+1);
        i = storeMapper.updateById(store);
        if(i==0){
            throw new RuntimeException("收藏失败");
        }
    }

    @Override
    public PageResponseVo<StoreCollectionResponseVo> listStoreCollection(Integer customerId,Integer pageNum,Integer pageSize) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("customer_id",customerId);
        Page<StoreCollection> page =new Page<>(pageNum,pageSize);
        IPage<StoreCollection> collectionPage = storeCollectionMapper.selectPage(page,wrapper);
        //补充返回vo需要的内容
        List<StoreCollection> collections=collectionPage.getRecords();
        List<StoreCollectionResponseVo> collectionResponseVos = new ArrayList<>();
        for (StoreCollection storeCollection:collections) {
            if(storeCollection.getStatus()==1){
                StoreCollectionResponseVo responseVo= new StoreCollectionResponseVo();
                Store store=storeMapper.selectById(storeCollection.getStoreId());
                responseVo.setStoreCollectionId(storeCollection.getId());
                responseVo.setStoreId(storeCollection.getStoreId());
                responseVo.setStoreName(store.getStoreName());
                responseVo.setAvatarPath(store.getAvatarPath());
                responseVo.setDescription(store.getDescription());
                List<Product> productList = productMapper.selectList(new QueryWrapper<Product>().eq("store_id",store.getId()));
                List<String> imagePaths = new ArrayList<>();
                for (Product product:productList) {
                    if(imagePaths.size()<3){
                        imagePaths.add(product.getImagePath());
                    }
                    else break;
                }
                while (imagePaths.size()<3){
                    imagePaths.add("https://img1.baidu.com/it/u=429168432,2347606957&fm=26&fmt=auto&gp=0.jpg");
                }
                responseVo.setImagePaths(imagePaths);
                collectionResponseVos.add(responseVo);
            }
        }
        //封装PageResponseVo
        PageResponseVo<StoreCollectionResponseVo> responseVos=new PageResponseVo(collectionPage);
        responseVos.setData(collectionResponseVos);
        return responseVos;
    }

    @Override
    @Transactional
    public void deleteStoreCollection(Integer storeCollectionId) {
        StoreCollection storeCollection = storeCollectionMapper.selectById(storeCollectionId);
        if(storeCollection==null){
            throw new RuntimeException("该收藏已取消");
        }
        storeCollectionMapper.deleteById(storeCollectionId);
        Store store = storeMapper.selectById(storeCollection.getStoreId());
        store.setFansNum(store.getFansNum()-1);
        int i = storeMapper.updateById(store);
        if(i==0){
            throw new RuntimeException("取消收藏失败");
        }
    }

    @Override
    @Transactional
    public void switchStoreCollection(Integer customerId, Integer storeId, Integer status) {
        StoreCollection storeCollection= storeCollectionMapper.selectOne(new QueryWrapper<StoreCollection>().eq("customer_id",customerId).eq("store_id",storeId));
        //第一次收藏，先插入数据
        if(storeCollection==null){
            storeCollection = new StoreCollection();
            storeCollection.setCustomerId(customerId);
            storeCollection.setStoreId(storeId);
            storeCollection.setGmtCreate(LocalDateTime.now());
            storeCollection.setStatus(1);
            //插入数据库
            int i=storeCollectionMapper.insert(storeCollection);
            if(i==0){
                throw new RuntimeException("收藏失败");
            }
            //增加商店粉丝数
            Store store = storeMapper.selectById(storeId);
            store.setFansNum(store.getFansNum()+1);
            i = storeMapper.updateById(store);
            if(i==0){
                throw new RuntimeException("收藏失败");
            }
        }
        //不是第一次收藏，对状态做出更改
        else {
            storeCollection.setStatus(status);
            int i=storeCollectionMapper.updateById(storeCollection);
            if(i==0){
                throw new RuntimeException("更改失败");
            }
            if(status==1){
                //增加粉丝数
                Store store = storeMapper.selectById(storeId);
                store.setFansNum(store.getFansNum()+1);
                i = storeMapper.updateById(store);
                if(i==0){
                    throw new RuntimeException("收藏失败");
                }
            }
            else {
                //减少粉丝数
                Store store = storeMapper.selectById(storeId);
                store.setFansNum(store.getFansNum()-1);
                i = storeMapper.updateById(store);
                if(i==0){
                    throw new RuntimeException("取消收藏失败");
                }
            }
        }
    }

    @Override
    public int checkStoreCollection(Integer customerId, Integer storeId) {
        //返回1代表已收藏，返回0代表未收藏
        StoreCollection storeCollection = storeCollectionMapper.selectOne(new QueryWrapper<StoreCollection>().eq("customer_id",customerId).eq("store_id",storeId).eq("status",1));
        if(storeCollection==null){
            return 0;
        }
        else{
            return 1;
        }
    }
}
