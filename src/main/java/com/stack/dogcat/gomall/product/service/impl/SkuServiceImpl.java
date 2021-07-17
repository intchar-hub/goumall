package com.stack.dogcat.gomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.product.responseVo.SkuQueryResponseVo;
import com.stack.dogcat.gomall.product.service.ISkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品规格和库存量单元 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

    @Autowired
    SkuMapper skuMapper;

    /**
     * 消费者根据商品id查看商品规格
     * @param productId
     * @return
     */
    @Override
    public List<SkuQueryResponseVo> listSkuByProductId(Integer productId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", productId);
        List<Sku> skusDB = skuMapper.selectList(queryWrapper);
        List<SkuQueryResponseVo> responseVos = CopyUtil.copyList(skusDB, SkuQueryResponseVo.class);
        return responseVos;
    }
}
