package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.responseVo.ProductQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IPmsRecommendProductService;
import com.stack.dogcat.gomall.product.service.impl.PmsRecommendProductServiceImpl;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/product/pms-recommend-product")
public class PmsRecommendProductController {

    @Autowired
    IPmsRecommendProductService pmsRecommendProductService;

    @GetMapping("/listRecommendProductByCustomer")
    public SysResult listRecommendProductByCustomer(Integer customerId, Integer pageNum, Integer pageSize){
        try{
            PageResponseVo<ProductQueryResponseVo> responseVo= pmsRecommendProductService.listRecommendProductByCustomer(customerId,pageNum,pageSize);
            return SysResult.success(responseVo);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知错误");
        }
    }

}
