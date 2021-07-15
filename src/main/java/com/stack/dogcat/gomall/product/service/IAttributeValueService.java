package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.entity.AttributeValue;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 属性值 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IAttributeValueService extends IService<AttributeValue> {

    void saveAttributeValue(Integer attributeNameId, String value);
    void deleteAttributeValueById(Integer attributeValueId);

}
