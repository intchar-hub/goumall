package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.product.entity.Sku;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.product.requestVo.SkuUpdateRequestVo;
import com.stack.dogcat.gomall.product.responseVo.SkuQueryResponseVo;

import java.util.List;

/**
 * <p>
 * 商品规格和库存量单元 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface ISkuService extends IService<Sku> {

    List<SkuQueryResponseVo> listSkuByProductId(Integer productId);

    void updateSku(List<SkuUpdateRequestVo> requestVos);
}
