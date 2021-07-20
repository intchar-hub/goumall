package com.stack.dogcat.gomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.ProductType;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.ProductTypeMapper;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeChild;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IProductTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private static final Logger LOG = LoggerFactory.getLogger(ProductTypeServiceImpl.class);

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Autowired
    ProductMapper productMapper;

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

    /**
     * 商家查看商品分类
     * @return
     */
    @Override
    public List<ProductTypeQueryResponseVo> listTypesByStore(Integer storeId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", storeId);
        List<Product> productsDB = productMapper.selectList(queryWrapper);
        if(productsDB == null || productsDB.size() == 0) {
            LOG.info("店铺内商品获取失败");
            throw new RuntimeException();
        }

        Set<Integer> set = new HashSet<>();
        for (Product product : productsDB) {
            set.add(product.getTypeId());
        }

        List<ProductType> types = productTypeMapper.selectBatchIds(set);
        for (ProductType type : types) {
            set.add(type.getParentId());
        }

        List<ProductType> productTypesDB = productTypeMapper.selectBatchIds(set);

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
