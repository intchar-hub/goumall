package com.stack.dogcat.gomall.product.service.impl;

import com.stack.dogcat.gomall.product.entity.ProductType;
import com.stack.dogcat.gomall.product.mapper.ProductTypeMapper;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeChild;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IProductTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    ProductTypeMapper productTypeMapper;

    /**
     * 用户查看商品分类
     * @return
     */
    @Override
    public List<ProductTypeQueryResponseVo> listTypes() {

        List<ProductType> productTypesDB = productTypeMapper.selectList(null);

        List<ProductTypeQueryResponseVo> responseVos = new ArrayList<>();

        //存放一级分类
        List<ProductType> firstLevelTypes = new ArrayList<>();

        //存放二级分类
        List<ProductType> secondLevelTypes = new ArrayList<>();

        for (ProductType productType : productTypesDB) {
            if(productType.getParentId() == 0) {
                firstLevelTypes.add(productType);
            } else {
                secondLevelTypes.add(productType);
            }
        }

        for (ProductType parentType : firstLevelTypes) {
            ProductTypeQueryResponseVo vo = new ProductTypeQueryResponseVo();
            List<ProductTypeChild> children = new ArrayList<>(); //记录当前父分类的子分类
            vo.setId(parentType.getId());
            vo.setName(parentType.getName());

            //已归类的分类
            List<ProductType> classified = new ArrayList<>();

            for (ProductType childType : secondLevelTypes) {
                if(childType.getParentId() == parentType.getId()) {
                    ProductTypeChild child = new ProductTypeChild();
                    child.setId(childType.getId());
                    child.setName(childType.getName());

                    children.add(child);
                    classified.add(childType);
                }
            }
            vo.setChildren(children);
            responseVos.add(vo);
            secondLevelTypes.removeAll(classified);
        }

        return responseVos;
    }
}
