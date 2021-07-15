package com.stack.dogcat.gomall.product.service.impl;

import com.stack.dogcat.gomall.product.entity.AttributeCollection;
import com.stack.dogcat.gomall.product.entity.AttributeName;
import com.stack.dogcat.gomall.product.entity.AttributeValue;
import com.stack.dogcat.gomall.product.mapper.AttributeCollectionMapper;
import com.stack.dogcat.gomall.product.mapper.AttributeNameMapper;
import com.stack.dogcat.gomall.product.mapper.AttributeValueMapper;
import com.stack.dogcat.gomall.product.service.IAttributeNameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <p>
 * 属性名 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class AttributeNameServiceImpl extends ServiceImpl<AttributeNameMapper, AttributeName> implements IAttributeNameService {

    @Autowired
    AttributeNameMapper attributeNameMapper;

    @Autowired
    AttributeCollectionMapper attributeCollectionMapper;

    @Autowired
    AttributeValueMapper attributeValueMapper;

    @Override
    @Transactional
    public void insertAttributeNameManualInput(AttributeName attributeName){

        attributeNameMapper.insert(attributeName);
        //属性集合中属性数量+1
        AttributeCollection attributeCollection = attributeCollectionMapper.selectById(attributeName.getAttributeCollectionId());
        Integer attributeNum=attributeCollection.getAttributeNum()+1;
        attributeCollection.setAttributeNum(attributeNum);
        attributeCollectionMapper.updateById(attributeCollection);

    }

    @Override
    @Transactional
    public  void insertAttributeNameAndValueList(AttributeName attributeName, ArrayList<String> valueArray){

        attributeNameMapper.insert(attributeName);
        //添加属性值列表
        Integer attributeNameId=attributeName.getId();
        for(int i=0;i<valueArray.size();i++){
            AttributeValue attributeValue =new AttributeValue();
            attributeValue.setAttributeNameId(attributeNameId);
            attributeValue.setValue(valueArray.get(i));
            attributeValue.setGmtCreate(LocalDateTime.now());
            attributeValueMapper.insert(attributeValue);
        }
        //属性集合中属性数量+1
        AttributeCollection attributeCollection = attributeCollectionMapper.selectById(attributeName.getAttributeCollectionId());
        Integer attributeNum=attributeCollection.getAttributeNum()+1;
        attributeCollection.setAttributeNum(attributeNum);
        attributeCollectionMapper.updateById(attributeCollection);

    }

}
