package com.stack.dogcat.gomall.sales.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.sales.entity.SalesPromotion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.sales.requestVo.SalesPromotionSaveRequestVo;
import com.stack.dogcat.gomall.sales.responseVo.SalesProductQueryResponseVo;
import com.stack.dogcat.gomall.sales.responseVo.SalesPromotionQueryResponseVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface ISalesPromotionService extends IService<SalesPromotion> {

    void savePromotion(SalesPromotionSaveRequestVo requestVo);

    PageResponseVo<SalesPromotionQueryResponseVo> listPromotionByStore(Integer pageNum, Integer pageSize, Integer storeId, Integer screenCondition);

    List<SalesPromotionQueryResponseVo> listPromotion(Integer screenCondition);

    PageResponseVo<SalesProductQueryResponseVo> listPromotionProducts(Integer pageNum, Integer pageSize);

    /**顾客参加秒杀活动*/
    String rushSalesByProductId(Map<String,Integer> map);

    /**秒杀活动判断是否有商品秒杀库存*/
    Integer hasStock(Integer productId);

    /**秒杀活动后减商品秒杀库存*/
    void reduceStock(Integer productId);

    /**秒杀活动*/
    String secKill(Integer customerId,Integer productId);

}
