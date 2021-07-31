package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.product.responseVo.ProductQueryResponseVo;
import com.stack.dogcat.gomall.product.requestVo.ProductSaveRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ProductUpdateRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ScreenProductsRequestVo;
import com.stack.dogcat.gomall.product.responseVo.ProductWithCommentResponseVo;
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

    PageResponseVo<ProductWithCommentResponseVo> listProductsByStore(Integer id, Integer pageNum, Integer pageSize);

    PageResponseVo<ProductQueryResponseVo> screenProducts(ScreenProductsRequestVo requestVo);

    PageResponseVo<ProductQueryResponseVo> listProductsByType(Integer typeId, Integer pageNum, Integer pageSize);

    PageResponseVo<ProductQueryResponseVo> listProductsByProductName(String name, Integer pageNum, Integer pageSize);

    PageResponseVo<ProductQueryResponseVo> listProductsByStoreId(Integer storeId, Integer pageNum, Integer pageSize);

    ProductWithStoreQueryResponseVo getProductWithStoreById(Integer id);

    PageResponseVo<ProductQueryResponseVo> listProductsBySalesNum(Integer pageNum, Integer pageSize);

    ProductQueryResponseVo getProductById(Integer id);

    List<ProductWithStoreQueryResponseVo> getProductWithStoreByIds(String ids);

    PageResponseVo<ProductQueryResponseVo> listNewProducts(Integer pageNum, Integer pageSize);
}
