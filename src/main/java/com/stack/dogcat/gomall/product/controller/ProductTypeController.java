package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.entity.ProductType;
import com.stack.dogcat.gomall.product.responseVo.ProductTypeQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
     * 顾客查看分类
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
     * 商家查看分类
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

}
