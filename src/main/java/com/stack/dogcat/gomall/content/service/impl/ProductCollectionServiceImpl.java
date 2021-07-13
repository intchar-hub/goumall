package com.stack.dogcat.gomall.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.content.entity.ProductCollection;
import com.stack.dogcat.gomall.content.mapper.ProductCollectionMapper;
import com.stack.dogcat.gomall.content.responseVo.ProductCollectionResponseVo;
import com.stack.dogcat.gomall.content.service.IProductCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品收藏 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ProductCollectionServiceImpl extends ServiceImpl<ProductCollectionMapper, ProductCollection> implements IProductCollectionService {

    @Autowired
    ProductCollectionMapper productCollectionMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    public void saveProductCollection(Integer customerId,Integer productId){
        ProductCollection productCollection=new ProductCollection();
        productCollection.setCustomerId(customerId);
        productCollection.setProductId(productId);
        productCollection.setGmtCreate(LocalDateTime.now());
        //插入数据库
        productCollectionMapper.insert(productCollection);
    }

    @Override
    public List<ProductCollectionResponseVo> listProductCollection(Integer customerId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("customer_id",customerId);
        List<ProductCollection> collections=productCollectionMapper.selectList(wrapper);
        List<ProductCollectionResponseVo> collectionResponseVos = new ArrayList<>();

        for (ProductCollection productCollection:collections) {
            ProductCollectionResponseVo responseVo= new ProductCollectionResponseVo();
            responseVo.setProductCollectionId(productCollection.getId());
            responseVo.setProductId(productCollection.getProductId());
            responseVo.setProductName(productMapper.selectById(productCollection.getProductId()).getName());
            responseVo.setImagePath(productMapper.selectById(productCollection.getProductId()).getImagePath());
            responseVo.setDescription(productMapper.selectById(productCollection.getProductId()).getDescription());
            collectionResponseVos.add(responseVo);
        }
        return collectionResponseVos;
    }

    @Override
    public void deleteProductCollection(Integer productCollectionId){
        ProductCollection productCollection = productCollectionMapper.selectById(productCollectionId);
        if (productCollection == null) {
            throw new RuntimeException("该收藏已取消");
        }
        productCollectionMapper.deleteById(productCollectionId);
    }

}
