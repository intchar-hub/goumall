package com.stack.dogcat.gomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.AttributeCollection;
import com.stack.dogcat.gomall.product.entity.AttributeName;
import com.stack.dogcat.gomall.product.entity.AttributeValue;
import com.stack.dogcat.gomall.product.mapper.AttributeCollectionMapper;
import com.stack.dogcat.gomall.product.mapper.AttributeNameMapper;
import com.stack.dogcat.gomall.product.mapper.AttributeValueMapper;
import com.stack.dogcat.gomall.product.responseVo.StoreAttribute;
import com.stack.dogcat.gomall.product.responseVo.AttributeCollectionResopnseVo;
import com.stack.dogcat.gomall.product.responseVo.StoreAttributesListResponseVo;
import com.stack.dogcat.gomall.product.responseVo.StoreValue;
import com.stack.dogcat.gomall.product.service.IAttributeCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class AttributeCollectionServiceImpl extends ServiceImpl<AttributeCollectionMapper, AttributeCollection> implements IAttributeCollectionService {

   @Autowired
   AttributeCollectionMapper attributeCollectionMapper;

   @Autowired
    AttributeNameMapper attributeNameMapper;

   @Autowired
    AttributeValueMapper attributeValueMapper;

    @Override
    public void saveAttributeCollection(Integer storeId ,String name ){
        AttributeCollection attributeCollection=new AttributeCollection();
        attributeCollection.setStoreId(storeId);
        attributeCollection.setName(name);
        attributeCollection.setAttributeNum(0);
        attributeCollection.setGmtCreate(LocalDateTime.now());
        int i=attributeCollectionMapper.insert(attributeCollection);
        if(i==0){
            throw new RuntimeException("新建属性集失败");
        }
    }

    @Override
    public PageResponseVo<AttributeCollectionResopnseVo> listAttributeCollections(Integer storeId, Integer pageNum, Integer pageSize) {
        Page<AttributeCollection> page = new Page<>(pageNum,pageSize);
        IPage<AttributeCollection> collectionPage=attributeCollectionMapper.selectPage(page,new QueryWrapper<AttributeCollection>().eq("store_id",storeId));
        PageResponseVo<AttributeCollectionResopnseVo> collectionResponseVo = new PageResponseVo(collectionPage);
        collectionResponseVo.setData(CopyUtil.copyList(collectionResponseVo.getData(),AttributeCollectionResopnseVo.class));
        return collectionResponseVo;
    }

    @Override
    public List<StoreAttributesListResponseVo> listAttributesByStore(Integer storeId) {
        List<AttributeCollection> collections = attributeCollectionMapper.selectList(new QueryWrapper<AttributeCollection>().eq("store_id",storeId));
        List<StoreAttributesListResponseVo> responseVos=new ArrayList<>();

        for (AttributeCollection collection:collections) {
            StoreAttributesListResponseVo responseVo = new StoreAttributesListResponseVo();
            responseVo.setId(collection.getId());
            responseVo.setName(collection.getName());

            List<AttributeName> attributeNames = attributeNameMapper.selectList(new QueryWrapper<AttributeName>().eq("attribute_collection_id",collection.getId()));
            List<StoreAttribute> attributes = new ArrayList<>();
            for (AttributeName attributeName:attributeNames) {
                List<StoreValue> values = new ArrayList<>();
                StoreAttribute attribute = new StoreAttribute();
                attribute.setId(attributeName.getId());
                attribute.setName(attributeName.getName());
                List<AttributeValue> attributeValues = attributeValueMapper.selectList(new QueryWrapper<AttributeValue>().eq("attribute_name_id",attributeName.getId()));
                for (AttributeValue attributeValue:attributeValues) {
                    StoreValue value = new StoreValue();
                    value.setId(attributeValue.getId());
                    value.setName(attributeValue.getValue());
                    values.add(value);
                }
                attribute.setValues(values);
                attributes.add(attribute);
            }
            responseVo.setAttributes(attributes);
            responseVos.add(responseVo);
        }
        return responseVos;
    }
}
