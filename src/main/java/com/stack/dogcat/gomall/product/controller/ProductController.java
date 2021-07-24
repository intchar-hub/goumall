package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.controller.OrderController;
import com.stack.dogcat.gomall.product.responseVo.ProductQueryResponseVo;
import com.stack.dogcat.gomall.product.requestVo.ProductSaveRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ProductUpdateRequestVo;
import com.stack.dogcat.gomall.product.requestVo.ScreenProductsRequestVo;
import com.stack.dogcat.gomall.product.responseVo.ProductWithCommentResponseVo;
import com.stack.dogcat.gomall.product.responseVo.ProductWithStoreQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 商品 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/product/product")
public class ProductController {

    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    IProductService productService;

    /**
     * 商家上架商品
     * @param requestVo
     * @return
     */
    @PostMapping("/saveProduct")
    public SysResult saveProduct(@Valid @RequestBody ProductSaveRequestVo requestVo) {
        try {
            productService.saveProduct(requestVo);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("商品上架失败");
        }
        return SysResult.success();
    }

    /**
     * 商家修改商品信息
     * @param requestVo
     * @return
     */
    @PostMapping("/updateProducts")
    public SysResult updateProducts(@Valid @RequestBody ProductUpdateRequestVo requestVo) {
        try {
            productService.updateProducts(requestVo);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("商品信息修改失败");
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

    /**
     * 商家查看商品信息
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/listProductsByStore")
    public SysResult listProductsByStore(Integer id, Integer pageNum, Integer pageSize) {
        PageResponseVo<ProductWithCommentResponseVo> pageResponseVo = null;
        try {
            pageResponseVo = productService.listProductsByStore(id, pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("商品信息获取失败");
        }
        return SysResult.success(pageResponseVo);
    }

    /**
     * 商家筛选查看商品
     * @param requestVo
     * @return
     */
    @GetMapping("/screenProducts")
    public SysResult screenProducts(@Valid ScreenProductsRequestVo requestVo) {
        PageResponseVo<ProductQueryResponseVo> pageResponseVo = null;
        try {
            pageResponseVo = productService.screenProducts(requestVo);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("商品筛选失败");
        }
        return SysResult.success(pageResponseVo);
    }

    /**
     * 顾客按分类查看商品
     * @param typeId
     * @return
     */
    @GetMapping("/listProductsByType")
    public SysResult listProductsByType(Integer typeId, Integer pageNum, Integer pageSize) {
        PageResponseVo<ProductQueryResponseVo> responseVos = null;
        try {
            responseVos = productService.listProductsByType(typeId, pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("分类商品获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 消费者查看商品（首页，一般用于未登录用户，按销量排序）
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/listProductsBySalesNum")
    public SysResult listProductsBySalesNum(Integer pageNum, Integer pageSize) {
        PageResponseVo<ProductQueryResponseVo> responseVos = null;
        try {
            responseVos = productService.listProductsBySalesNum(pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("分类商品获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 顾客按商品名搜索商品
     * @param name
     * @return
     */
    @GetMapping("/listProductsByProductName")
    public SysResult listProductsByProductName(Integer customerId,String name, Integer pageNum, Integer pageSize) {
        PageResponseVo<ProductQueryResponseVo> responseVos = null;
        try {
            responseVos = productService.listProductsByProductName(name, pageNum, pageSize);
            if(customerId!=0){
                logger.info("search_pro->current_customer.id:{},search_pro.name:{}",new Object[]{customerId.toString(), name});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("按名字获取商品失败");
        }

        return SysResult.success(responseVos);
    }

    /**
     * 顾客按店铺搜索商品
     * @param storeId
     * @return
     */
    @GetMapping("/listProductsByStoreId")
    public SysResult listProductsByStoreId(Integer customerId,Integer storeId, Integer pageNum, Integer pageSize) {
        PageResponseVo<ProductQueryResponseVo> responseVos = null;
        try {
            responseVos = productService.listProductsByStoreId(storeId, pageNum, pageSize);
            if(customerId!=0){
                logger.info("search_store->current_customer.id:{},search_store.id:{}",new Object[]{customerId.toString(), storeId.toString()});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("按店铺获取商品失败");
        }

        return SysResult.success(responseVos);
    }

    /**
     * 顾客按商品id查看商品（和店铺）信息
     * @param id 商品id
     * @return
     */
    @GetMapping("/getProductWithStoreById")
    public SysResult getProductWithStoreById(Integer customerId,Integer id) {
        ProductWithStoreQueryResponseVo responseVo = null;
        try {
            responseVo = productService.getProductWithStoreById(id);
//            logger.info("click_pro->current_customer.id:{},click_pro.id:{},",new Object[]{customerId.toString(),id.toString()});
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("获取商品及店铺失败");
        }
        return SysResult.success(responseVo);
    }

    /**
     * 按id查看商品
     * @param id 商品id
     * @return
     */
    @GetMapping("/getProductById")
    public SysResult getProductById(Integer id) {
        ProductQueryResponseVo responseVo = null;
        try {
            responseVo = productService.getProductById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("获取商品失败");
        }
        return SysResult.success(responseVo);
    }

}
