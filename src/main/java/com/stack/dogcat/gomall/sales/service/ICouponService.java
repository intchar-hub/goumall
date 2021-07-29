package com.stack.dogcat.gomall.sales.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.sales.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.sales.requestVo.CouponSaveRequestVo;
import com.stack.dogcat.gomall.sales.responseVo.CouponInfoResponseVo;

import java.util.List;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface ICouponService extends IService<Coupon> {

    void saveCoupon(CouponSaveRequestVo requestVo);
    PageResponseVo<CouponInfoResponseVo> listCouponByStore(Integer storeId,Integer screenCondition,Integer pageNum,Integer pageSize);
    PageResponseVo<CouponInfoResponseVo> listCoupon(Integer  customerId,Integer screenCondition, Integer pageNum, Integer pageSize);
    void updateCouponStatus(Integer customerId,Integer couponId,Integer status);

}
