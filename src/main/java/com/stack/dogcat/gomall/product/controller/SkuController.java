package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.requestVo.SkuUpdateRequestVo;
import com.stack.dogcat.gomall.product.responseVo.SkuQueryResponseVo;
import com.stack.dogcat.gomall.product.service.ISkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品规格和库存量单元 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/product/sku")
public class SkuController {

    @Autowired
    ISkuService skuService;

    /**
     * 消费者根据商品id查看商品规格
     * @param productId
     * @return
     */
    @GetMapping("/listSkuByProductId")
    public SysResult listSkuByProductId(Integer productId) {
        List<SkuQueryResponseVo> responseVos = null;
        try {
            responseVos = skuService.listSkuByProductId(productId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("商品规格获取失败");
        }
        return SysResult.success(responseVos);
    }


    /**
     * 修改某一商品的sku
     * @param requestVos
     * @return
     */
    @PostMapping("/updateSku")
    public SysResult updateSku(@RequestBody List<SkuUpdateRequestVo> requestVos) {
        try {
            skuService.updateSku(requestVos);
        } catch (Exception e) {
            return SysResult.error("操作失败");
        }
        return SysResult.success();
    }

}
