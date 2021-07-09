package com.stack.dogcat.gomall.product.service.impl;

import com.stack.dogcat.gomall.product.entity.ProductType;
import com.stack.dogcat.gomall.product.mapper.ProductTypeMapper;
import com.stack.dogcat.gomall.product.service.IProductTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品类型 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {

}
