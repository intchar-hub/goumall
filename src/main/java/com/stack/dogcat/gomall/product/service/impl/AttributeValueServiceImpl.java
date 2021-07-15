package com.stack.dogcat.gomall.product.service.impl;

import com.stack.dogcat.gomall.product.entity.AttributeValue;
import com.stack.dogcat.gomall.product.mapper.AttributeValueMapper;
import com.stack.dogcat.gomall.product.service.IAttributeValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 属性值 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class AttributeValueServiceImpl extends ServiceImpl<AttributeValueMapper, AttributeValue> implements IAttributeValueService {

    @Autowired
    AttributeValueMapper attributeValueMapper;

    @Override
    public void saveAttributeValue(Integer attributeNameId, String value) {
        AttributeValue attributeValue=new AttributeValue();
        attributeValue.setAttributeNameId(attributeNameId);
        attributeValue.setValue(value);
        attributeValue.setGmtCreate(LocalDateTime.now());
        int i=attributeValueMapper.insert(attributeValue);
        if(i==0){
            throw new RuntimeException("新建属性失败");
        }
    }

    @Override
    public void deleteAttributeValueById(Integer attributeValueId) {
        int i=attributeValueMapper.deleteById(attributeValueId);
        if(i==0){
            throw new RuntimeException("删除失败");
        }
    }
}
