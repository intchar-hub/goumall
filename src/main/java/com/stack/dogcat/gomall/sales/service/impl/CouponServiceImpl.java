package com.stack.dogcat.gomall.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.sales.entity.Coupon;
import com.stack.dogcat.gomall.sales.mapper.CouponMapper;
import com.stack.dogcat.gomall.sales.requestVo.CouponSaveRequestVo;
import com.stack.dogcat.gomall.sales.responseVo.CouponInfoResponseVo;
import com.stack.dogcat.gomall.sales.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 优惠券表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {

    @Autowired
    CouponMapper couponMapper;

    @Override
    public void saveCoupon(CouponSaveRequestVo requestVo) {
        Coupon coupon = new Coupon();
        coupon.setStoreId(requestVo.getStoreId());
        coupon.setTargetPrice(requestVo.getTargetPrice());
        coupon.setDiscount(requestVo.getDiscount());
        coupon.setStartTime(requestVo.getStartTime());
        coupon.setDeadline(requestVo.getDeadline());
        coupon.setGmtCreate(LocalDateTime.now());

        int i = couponMapper.insert(coupon);
        if(i==0){
            throw new RuntimeException("插入失败");
        }
    }

    @Override
    public PageResponseVo<CouponInfoResponseVo> listCouponByStore(Integer storeId, Integer screenCondition, Integer pageNum, Integer pageSize) {
        Page<Coupon> page = new Page<>(pageNum,pageSize);
        IPage<Coupon> couponPage =new Page<>();
        if(screenCondition==0){
            couponPage = couponMapper.selectPage(page,null);
        }
        else if(screenCondition==1){
            couponPage = couponMapper.selectPage(page,new QueryWrapper<Coupon>()
                    .eq("store_id",storeId)
                    .le("deadline",LocalDateTime.now())
                    .or()
                    .gt("start_time",LocalDateTime.now()));
        }
        else {
            couponPage = couponMapper.selectPage(page,new QueryWrapper<Coupon>()
                    .eq("store_id",storeId)
                    .gt("deadline",LocalDateTime.now())
                    .le("start_time",LocalDateTime.now()));
        }
        if(couponPage==null){
            throw new RuntimeException("查询失败");
        }
        PageResponseVo<CouponInfoResponseVo> couponInfoResponseVos = new PageResponseVo(couponPage);
        couponInfoResponseVos.setData(CopyUtil.copyList(couponInfoResponseVos.getData(),CouponInfoResponseVo.class));
        if(couponInfoResponseVos.getData()==null){
            throw new RuntimeException("转化失败");
        }
        return couponInfoResponseVos;
    }

    @Override
    public List<CouponInfoResponseVo> listCoupon(Integer screenCondition) {
        List<Coupon> coupons=new ArrayList<>();
        if(screenCondition==0){
            coupons=couponMapper.selectList(null);
        }
        else if(screenCondition==1){
            coupons=couponMapper.selectList(new QueryWrapper<Coupon>().le("deadline",LocalDateTime.now()).
                    or().gt("start_time",LocalDateTime.now()));
        }
        else{
            coupons=couponMapper.selectList(new QueryWrapper<Coupon>().gt("deadline",LocalDateTime.now())
                                                                        .le("start_time",LocalDateTime.now()));
        }
        if(coupons==null){
            throw new RuntimeException("查询失败");
        }
        List<CouponInfoResponseVo> responseVos=new ArrayList<>();
        responseVos= CopyUtil.copyList(coupons,CouponInfoResponseVo.class);
        if(responseVos.size()==0){
            throw new RuntimeException("转化失败");
        }
        return responseVos;
    }
}
