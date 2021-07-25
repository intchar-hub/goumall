package com.stack.dogcat.gomall.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.entity.ProductCollection;
import com.stack.dogcat.gomall.content.entity.StoreCollection;
import com.stack.dogcat.gomall.content.mapper.ProductCollectionMapper;
import com.stack.dogcat.gomall.content.responseVo.ProductCollectionResponseVo;
import com.stack.dogcat.gomall.content.service.IProductCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        int i=productCollectionMapper.insert(productCollection);
        if(i==0){
            throw new RuntimeException("收藏失败");
        }

    }

    @Override
    public PageResponseVo<ProductCollectionResponseVo> listProductCollection(Integer customerId, Integer pageNum, Integer pageSzie){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("customer_id",customerId);
        Page<ProductCollection> page =new Page<>(pageNum,pageSzie);
        IPage<ProductCollection> collectionPage=productCollectionMapper.selectPage(page,wrapper);
        //补充返回vo需要的内容
        List<ProductCollection> collections = collectionPage.getRecords();
        List<ProductCollectionResponseVo> collectionResponseVos = new ArrayList<>();
        for (ProductCollection productCollection:collections) {
            ProductCollectionResponseVo responseVo= new ProductCollectionResponseVo();
            Product product=productMapper.selectById(productCollection.getProductId());
            responseVo.setProductCollectionId(productCollection.getId());
            responseVo.setProductId(productCollection.getProductId());
            responseVo.setProductName(product.getName());
            responseVo.setImagePath(product.getImagePath());
            responseVo.setDescription(product.getDescription());
            responseVo.setHighestPrice(product.getHighestPrice());
            responseVo.setLowestPrice(product.getLowestPrice());
            collectionResponseVos.add(responseVo);
        }
        //封装PageResponseVo
        PageResponseVo<ProductCollectionResponseVo> responseVos = new PageResponseVo(collectionPage);
        responseVos.setData(collectionResponseVos);
        return responseVos;
    }

    @Override
    public void deleteProductCollection(Integer productCollectionId){
        ProductCollection productCollection = productCollectionMapper.selectById(productCollectionId);
        if (productCollection == null) {
            throw new RuntimeException("该收藏已取消");
        }
        productCollectionMapper.deleteById(productCollectionId);
    }

    @Override
    public void switchProductCollection(Integer customerId, Integer productId, Integer status) {
        ProductCollection productCollection= productCollectionMapper.selectOne(new QueryWrapper<ProductCollection>().eq("customer_id",customerId).eq("product_id",productId));
        //第一次收藏，先插入数据
        if(productCollection==null){
            productCollection = new ProductCollection();
            productCollection.setCustomerId(customerId);
            productCollection.setProductId(productId);
            productCollection.setGmtCreate(LocalDateTime.now());
            productCollection.setStatus(1);
            //插入数据库
            int i=productCollectionMapper.insert(productCollection);
            if(i==0){
                throw new RuntimeException("收藏失败");
            }
        }
        //不是第一次收藏，对状态做出更改
        else {
            productCollection.setStatus(status);
            int i=productCollectionMapper.updateById(productCollection);
            if(i==0){
                throw new RuntimeException("更改失败");
            }
        }
    }

    @Override
    public int checkProductCollection(Integer customerId, Integer productId) {
        //返回1代表已收藏，返回0代表未收藏
        ProductCollection productCollection = productCollectionMapper.selectOne(new QueryWrapper<ProductCollection>().eq("customer_id",customerId).eq("product_id",productId).eq("status",1));
        if(productCollection==null){
            return 0;
        }
        else{
            return 1;
        }
    }
}
