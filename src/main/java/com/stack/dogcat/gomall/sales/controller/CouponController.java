package com.stack.dogcat.gomall.sales.controller;


import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.sales.requestVo.CouponSaveRequestVo;
import com.stack.dogcat.gomall.sales.responseVo.CouponInfoResponseVo;
import com.stack.dogcat.gomall.sales.service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 优惠券表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/sales/coupon")
public class CouponController {

    @Autowired
    ICouponService couponService;

    /**
     * 商家发布优惠券
     */
    @PostMapping("/saveCoupon")
    public SysResult saveCoupon(@Valid @RequestBody CouponSaveRequestVo requestVo){
        try{
            couponService.saveCoupon(requestVo);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
        return SysResult.success();
    }

    /**
     * 商家查看自己发布的优惠券
     */
    @GetMapping("/listCouponByStore")
    public SysResult listCouponByStore(Integer storeId,Integer screenCondition,Integer pageNum,Integer pageSize){
        try{
            PageResponseVo<CouponInfoResponseVo> responseVos=couponService.listCouponByStore(storeId,screenCondition,pageNum,pageSize);
            return SysResult.success(responseVos);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
    }

    /**
     * 顾客查看所有优惠券
     */
    @GetMapping("/listCoupon")
    @Token.UserLoginToken
    public SysResult listCoupon(Integer  customerId,Integer screenCondition, Integer pageNum, Integer pageSize){
        try{
            PageResponseVo<CouponInfoResponseVo> responseVos=couponService.listCoupon(customerId,screenCondition,pageNum,pageSize);
            return SysResult.success(responseVos);
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
    }
    /**
     * 顾客使用优惠券
     */
    @PostMapping("/updateCouponStatus")
    public SysResult updateCouponStatus(Integer customerId, Integer couponId, Integer status){
        try{
            couponService.updateCouponStatus(customerId,couponId,status);
            return SysResult.success();
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error("未知错误");
        }
    }
}
