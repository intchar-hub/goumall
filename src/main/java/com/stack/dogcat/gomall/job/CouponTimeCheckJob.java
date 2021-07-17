package com.stack.dogcat.gomall.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.stack.dogcat.gomall.sales.entity.Coupon;
import com.stack.dogcat.gomall.sales.entity.UserCoupon;
import com.stack.dogcat.gomall.sales.mapper.CouponMapper;
import com.stack.dogcat.gomall.sales.mapper.UserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CouponTimeCheckJob {

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    UserCouponMapper userCouponMapper;

    @Scheduled(cron= "0 0/5 * * * ? ")
    public void checkCouponTime(){
        List<Coupon> coupons=couponMapper.selectList(
                new QueryWrapper<Coupon>().
                        le("deadline", LocalDateTime.now()).
                        or().gt("start_time",LocalDateTime.now()));
        for (Coupon coupon:coupons) {
            UpdateWrapper<UserCoupon> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("coupon_id",coupon.getId());
            updateWrapper.set("status",2);
            userCouponMapper.update(null,updateWrapper);
        }
    }

}
