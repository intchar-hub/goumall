package com.stack.dogcat.gomall.content.service;

import com.stack.dogcat.gomall.content.entity.ProductCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.content.responseVo.ProductCollectionResponseVo;

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
    List<ProductCollectionResponseVo> listProductCollection(Integer customerId);

}
