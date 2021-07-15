package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.requestVo.ProductSaveRequestVo;
import com.stack.dogcat.gomall.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/product/product")
public class ProductController {

    @Autowired
    IProductService productService;

    /**
     * 商家上架商品
     * @param requestVo
     * @return
     */
    @PostMapping("/saveProduct")
    public SysResult saveProduct(@RequestBody ProductSaveRequestVo requestVo) {
        try {
            productService.saveProduct(requestVo);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("商品上架失败");
        }
        return SysResult.success();
    }

    /**
     * 商家下架商品
     * @param id
     * @return
     */
    @DeleteMapping("/deleteProductById")
    public SysResult deleteProductById(Integer id) {
        try {
            productService.deleteProductById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("商品下架失败");
        }
        return SysResult.success();
    }

}
