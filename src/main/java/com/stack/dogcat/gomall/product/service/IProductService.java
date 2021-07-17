package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.product.responseVo.ProductQueryResponseVo;
import com.stack.dogcat.gomall.product.requestVo.ProductSaveRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ProductUpdateRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ScreenProductsRequestVo;
import com.stack.dogcat.gomall.product.responseVo.ProductWithStoreQueryResponseVo;

import java.util.List;

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

    PageResponseVo<ProductQueryResponseVo> listProductsByStore(Integer id, Integer pageNum, Integer pageSize);

    PageResponseVo<ProductQueryResponseVo> screenProducts(ScreenProductsRequestVo requestVo);

    List<ProductQueryResponseVo> listProductsByType(Integer typeId);

    List<ProductQueryResponseVo> listProductsByProductName(String name);

    List<ProductQueryResponseVo> listProductsByStoreId(Integer storeId);

    ProductWithStoreQueryResponseVo getProductWithStoreById(Integer id);
}
