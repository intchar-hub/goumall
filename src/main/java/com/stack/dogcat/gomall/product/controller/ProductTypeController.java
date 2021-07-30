package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.entity.ProductType;
import com.stack.dogcat.gomall.product.responseVo.FirstLevelTypeQueryResponseVo;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IProductTypeService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品类型 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/product/product-type")
public class ProductTypeController {

    @Autowired
    IProductTypeService productTypeService;

    /**
     * 顾客查看分类（不分页）
     * @return
     */
    @GetMapping("/listTypes")
    public SysResult listTypes() {
        List<ProductTypeQueryResponseVo> responseVos = null;
        try {
            responseVos = productTypeService.listTypes();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("分类获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 商家查看所有分类（分页）
     * @return
     */
    @GetMapping("/listTypesByPage")
    public SysResult listTypesByStore(Integer pageNum, Integer pageSize) {
        PageResponseVo<ProductTypeQueryResponseVo> responseVos = null;
        try {
            responseVos = productTypeService.listTypesByPage(pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("分类获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 商家查看店铺内分类（不分页）
     * @return
     */
    @GetMapping("/listTypesByStore")
    public SysResult listTypesByStore(Integer storeId) {
        List<ProductTypeQueryResponseVo> responseVos = null;
        try {
            responseVos = productTypeService.listTypesByStore(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("分类获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 根据子分类查询父分类
     * @param typeId
     * @return
     */
    @GetMapping("/getParentTypeId")
    public SysResult getParentTypeId(Integer typeId) {
        Integer parentId = 0;
        try {
            parentId = productTypeService.getParentTypeId(typeId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("分类获取失败");
        }
        return SysResult.success(parentId);
    }

    /**
     * 查询所有一级分类（不分页）
     * @return
     */
    @GetMapping("/listFirstLevelTypes")
    public SysResult listFirstLevelTypes() {
        List<FirstLevelTypeQueryResponseVo> responseVos = new ArrayList<>();
        try {
            responseVos = productTypeService.listFirstLevelTypes();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("一级分类获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 添加分类
     */
    @PostMapping("/saveProductType")
    public SysResult saveProductType(String name, Integer parentId) {
        try {
            productTypeService.saveProductType(name, parentId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("分类添加失败");
        }
        return SysResult.success();
    }
}
