package com.stack.dogcat.gomall.content.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.entity.ProductCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.content.responseVo.ProductCollectionResponseVo;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 * 商品收藏 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IProductCollectionService extends IService<ProductCollection> {

    void saveProductCollection(Integer customerId,Integer productId);
    PageResponseVo<ProductCollectionResponseVo> listProductCollection(Integer customerId, Integer pageNum, Integer pageSzie);
    void deleteProductCollection(Integer productCollectionId);
    void switchProductCollection (Integer customerId, Integer productId,Integer status);
    int checkProductCollection(Integer customerId,Integer productId);
}
