package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.product.entity.AttributeName;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;

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
    public void insertAttributeNameManualInput(AttributeName attributeName);

    //插入属性名，属性值列表
    public void insertAttributeNameAndValueList(AttributeName attributeName, ArrayList<String> valueArray);
}
