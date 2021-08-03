package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.PmsRecommendProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.product.responseVo.ProductQueryResponseVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrm
 * @since 2021-08-03
 */
public interface IPmsRecommendProductService extends IService<PmsRecommendProduct> {

    PageResponseVo<ProductQueryResponseVo> listRecommendProductByCustomer(Integer customerId,Integer pageNum,Integer pageSize);

}
