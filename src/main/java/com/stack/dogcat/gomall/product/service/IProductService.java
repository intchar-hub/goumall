package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.product.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.product.requestVo.ProductSaveRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ProductUpdateRequestVo;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IProductService extends IService<Product> {

    void saveProduct(ProductSaveRequestVo requestVo);

    void deleteProductById(Integer id);

    void updateProducts(ProductUpdateRequestVo requestVo);
}
