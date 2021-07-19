package com.stack.dogcat.gomall.sales.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.sales.requestVo.SalesPromotionSaveRequestVo;
import com.stack.dogcat.gomall.sales.responseVo.SalesPromotionQueryResponseVo;
import com.stack.dogcat.gomall.sales.service.ISalesPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/sales/sales-promotion")
public class SalesPromotionController {

    @Autowired
    ISalesPromotionService salesPromotionService;

    /**
     * 发布秒杀活动
     * @param requestVo
     * @return
     */
    @PostMapping("/savePromotion")
    public SysResult savePromotion(@Valid @RequestBody SalesPromotionSaveRequestVo requestVo) {
        try {
            salesPromotionService.savePromotion(requestVo);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("活动发布失败");
        }
        return SysResult.success();
    }

    /**
     * 商家查看店铺内秒杀活动
     * @param storeId
     * @param screenCondition
     * @return
     */
    @GetMapping("/listPromotionByStore")
    public SysResult listPromotionByStore(Integer pageNum, Integer pageSize, Integer storeId, Integer screenCondition) {
        PageResponseVo<SalesPromotionQueryResponseVo> responseVos = null;
        try {
            responseVos = salesPromotionService.listPromotionByStore(pageNum, pageSize, storeId, screenCondition);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("店铺内秒杀活动获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 顾客查看秒杀活动
     * @param screenCondition
     * @return
     */
    @GetMapping("/listPromotion")
    public SysResult listPromotion(Integer screenCondition) {
        List<SalesPromotionQueryResponseVo> responseVos = null;
        try {
            responseVos = salesPromotionService.listPromotion(screenCondition);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("秒杀活动获取失败");
        }
        return SysResult.success(responseVos);
    }

}
