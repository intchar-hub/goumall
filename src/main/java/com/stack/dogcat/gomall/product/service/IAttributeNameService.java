package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.AttributeName;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.product.responseVo.AttributeNameVo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 属性名 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IAttributeNameService extends IService<AttributeName> {

    //插入属性名(手工录入)
    void insertAttributeNameManualInput(AttributeName attributeName);

    //插入属性名，属性值列表
    void insertAttributeNameAndValueList(AttributeName attributeName, ArrayList<String> valueArray);

    //（分页）按属性集合查看所有商品属性
    PageResponseVo<AttributeNameVo> listAttributeByCollection(Integer collectionId, Integer pageNum, Integer pageSize);
}
