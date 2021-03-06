package com.stack.dogcat.gomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.ProductType;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.ProductTypeMapper;
import com.stack.dogcat.gomall.product.responseVo.FirstLevelTypeQueryResponseVo;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeChild;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IProductTypeService;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * 用户查看商品分类，不分页
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
     * 商家查看自家商品分类，不分页
     * @return
     */
    @Override
    public List<ProductTypeQueryResponseVo> listTypesByStore(Integer storeId) {
        if(storeId == null) {
            LOG.info("缺少请求参数");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", storeId);
        queryWrapper.eq("status", 1);
        List<Product> productsDB = productMapper.selectList(queryWrapper);
        if(productsDB == null || productsDB.size() == 0) {
            return new ArrayList<>();
//            LOG.info("店铺内商品获取失败");
//            throw new RuntimeException();
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

    /**
     * 根据子分类查询父分类
     * @param typeId
     * @return
     */
    @Override
    public Integer getParentTypeId(Integer typeId) {
        if(typeId == null) {
            LOG.info("缺少请求参数");
            throw new RuntimeException();
        }

        ProductType productTypeDB = productTypeMapper.selectById(typeId);
        if(productTypeDB == null) {
            LOG.info("该子分类不存在");
            throw new RuntimeException();
        }
        return productTypeDB.getParentId();
    }

    /**
     * 查看所有分类（分页）
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponseVo<ProductTypeQueryResponseVo> listTypesByPage(Integer pageNum, Integer pageSize) {
        Page<ProductType> page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", 0);
        IPage<ProductType> iPage = productTypeMapper.selectPage(page,queryWrapper);
        PageResponseVo<ProductTypeQueryResponseVo> pageResponseVo = new PageResponseVo(iPage);

        List<Integer> parentIds = new ArrayList<>();
        for (ProductType record : iPage.getRecords()) {
            parentIds.add(record.getId());
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.in("parent_id", parentIds);
        List<ProductType> productTypesDB = productTypeMapper.selectList(queryWrapper);

        List<ProductTypeQueryResponseVo> responseVos = new ArrayList<>();
        for (ProductType record : iPage.getRecords()) {
            ProductTypeQueryResponseVo vo = new ProductTypeQueryResponseVo();
            List<ProductTypeChild> children = new ArrayList<>();
            vo.setId(record.getId());
            vo.setName(record.getName());
            vo.setParentId(0);
            vo.setGmtCreate(record.getGmtCreate());

            //已归类的分类
            List<ProductType> classified = new ArrayList<>();

            for (ProductType productType : productTypesDB) {
                if(productType.getParentId() == record.getId()) {
                    ProductTypeChild child = new ProductTypeChild();
                    child.setId(productType.getId());
                    child.setName(productType.getName());
                    child.setParentId(productType.getParentId());
                    child.setGmtCreate(productType.getGmtCreate());

                    children.add(child);
                    classified.add(productType);
                }
            }
            vo.setChildren(children);
            responseVos.add(vo);
            productTypesDB.removeAll(classified);
        }

        pageResponseVo.setData(responseVos);

        return pageResponseVo;
    }

    /**
     * 查询所有一级分类（不分页）
     * @return
     */
    @Override
    public List<FirstLevelTypeQueryResponseVo> listFirstLevelTypes() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", 0);
        List<ProductType> productTypesDB = productTypeMapper.selectList(queryWrapper);
        List<FirstLevelTypeQueryResponseVo> responseVos = CopyUtil.copyList(productTypesDB, FirstLevelTypeQueryResponseVo.class);
        return responseVos;
    }

    /**
     * 添加分类
     * @param name
     * @param parentId
     */
    @Override
    public void saveProductType(String name, Integer parentId) {
        ProductType productType = new ProductType();
        productType.setGmtCreate(LocalDateTime.now());
        productType.setName(name);
        productType.setParentId(parentId);
        productTypeMapper.insert(productType);
    }
}
