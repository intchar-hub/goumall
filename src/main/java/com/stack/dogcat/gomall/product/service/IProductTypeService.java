package com.stack.dogcat.gomall.product.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.ProductType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.product.responseVo.FirstLevelTypeQueryResponseVo;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeQueryResponseVo;

import java.util.List;

/**
 * <p>
 * 商品类型 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IProductTypeService extends IService<ProductType> {

    List<ProductTypeQueryResponseVo> listTypes();

    List<ProductTypeQueryResponseVo> listTypesByStore(Integer storeId);

    Integer getParentTypeId(Integer typeId);

    PageResponseVo<ProductTypeQueryResponseVo> listTypesByPage(Integer pageNum, Integer pageSize);

    List<FirstLevelTypeQueryResponseVo> listFirstLevelTypes();

    void saveProductType(String name, Integer parentId);
}
