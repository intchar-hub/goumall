package com.stack.dogcat.gomall.product.service.impl;

import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
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
import com.stack.dogcat.gomall.product.responseVo.AttributeNameVo;
import com.stack.dogcat.gomall.product.service.IAttributeNameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @Override
    @Transactional
    public PageResponseVo<AttributeNameVo> listAttributeByCollection(Integer collectionId, Integer pageNum, Integer pageSize){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("attribute_collection_id", collectionId);
        Page<AttributeName> page = new Page(pageNum,pageSize);
        List<AttributeNameVo> attributeNameVoList=new ArrayList<>();
        IPage<AttributeName> attributeNamePage = attributeNameMapper.selectPage(page,queryWrapper);
        List<AttributeName> attributeNameList= attributeNamePage.getRecords();
        for(int i=0;i<attributeNameList.size();i++){
            AttributeNameVo attributeNameVo = new AttributeNameVo();
            attributeNameVo.setAttributeCollectionId(attributeNameList.get(i).getAttributeCollectionId());
            attributeNameVo.setAttributeName(attributeNameList.get(i).getName());
            attributeNameVo.setAttributeNameId(attributeNameList.get(i).getId());
            attributeNameVo.setGmtCreate(attributeNameList.get(i).getGmtCreate());
            attributeNameVo.setInputType(attributeNameList.get(i).getInputType());

            Integer attributeNameId=attributeNameList.get(i).getId();
            ArrayList<String> attributeValueStrList=new ArrayList<>();

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("attribute_name_id", attributeNameId);
            List<AttributeValue> attributeValueList;
            attributeValueList = attributeValueMapper.selectList(queryWrapper2);

            for(int j =0;j<attributeValueList.size();j++){
                attributeValueStrList.add(attributeValueList.get(j).getValue());
            }
            attributeNameVo.setAttributeValueList(attributeValueStrList);
            attributeNameVoList.add(attributeNameVo);
        }

        PageResponseVo<AttributeNameVo> pageResponseVo=new PageResponseVo(attributeNamePage);
        pageResponseVo.setData(attributeNameVoList);
        return pageResponseVo;
    }

    @Override
    @Transactional
    public AttributeNameVo listAttributeValueByName(Integer attributeNameId){

        AttributeNameVo responseVo =new AttributeNameVo();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", attributeNameId);
        AttributeName attributeName=attributeNameMapper.selectOne(queryWrapper);

        responseVo.setAttributeNameId(attributeNameId);
        responseVo.setAttributeName(attributeName.getName());
        responseVo.setAttributeCollectionId(attributeName.getAttributeCollectionId());
        responseVo.setInputType(attributeName.getInputType());
        responseVo.setGmtCreate(attributeName.getGmtCreate());

        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("attribute_name_id", attributeNameId);
        List<AttributeValue> attributeValueList;
        attributeValueList = attributeValueMapper.selectList(queryWrapper2);
        ArrayList<String> attributeValueStrList=new ArrayList<>();
        for(int j =0;j<attributeValueList.size();j++){
            attributeValueStrList.add(attributeValueList.get(j).getValue());
        }
        responseVo.setAttributeValueList(attributeValueStrList);

        return responseVo;
    }

    @Override
    @Transactional
    public void deleteAttributeNameById(Integer attributeNameId){

        attributeNameMapper.deleteById(attributeNameId);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("attribute_name_id", attributeNameId);
        attributeValueMapper.delete(queryWrapper);

    }
}
