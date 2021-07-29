package com.stack.dogcat.gomall.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.sales.entity.Coupon;
import com.stack.dogcat.gomall.sales.entity.UserCoupon;
import com.stack.dogcat.gomall.sales.mapper.CouponMapper;
import com.stack.dogcat.gomall.sales.mapper.UserCouponMapper;
import com.stack.dogcat.gomall.sales.requestVo.CouponSaveRequestVo;
import com.stack.dogcat.gomall.sales.responseVo.CouponInfoResponseVo;
import com.stack.dogcat.gomall.sales.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.entity.Customer;
import com.stack.dogcat.gomall.user.mapper.CustomerMapper;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    UserCouponMapper userCouponMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Override
    @Transactional
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
            throw new RuntimeException("插入优惠券失败");
        }

        List<Customer> customers = customerMapper.selectList(new QueryWrapper<Customer>().select("id"));
        for (Customer customer:customers) {
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setCustomerId(customer.getId());
            userCoupon.setCouponId(coupon.getId());
            i = userCouponMapper.insert(userCoupon);
            if(i==0){
                throw new RuntimeException("优惠券发送给顾客失败");
            }
        }
    }

    @Override
    public PageResponseVo<CouponInfoResponseVo> listCouponByStore(Integer storeId, Integer screenCondition, Integer pageNum, Integer pageSize) {
        Page<Coupon> page = new Page<>(pageNum,pageSize);
        IPage<Coupon> couponPage =new Page<>();
        //查询全部
        if(screenCondition==0){
            couponPage = couponMapper.selectPage(page,new QueryWrapper<Coupon>().eq("store_id",storeId));
        }
        //查询不能使用
        else if(screenCondition==1){
            couponPage = couponMapper.selectPage(page,new QueryWrapper<Coupon>()
                    .eq("store_id",storeId)
                    .le("deadline",LocalDateTime.now())
                    .or()
                    .gt("start_time",LocalDateTime.now()));
        }
        //查询可使用
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
    public PageResponseVo<CouponInfoResponseVo> listCoupon(Integer  customerId,Integer screenCondition, Integer pageNum, Integer pageSize) {
        List<CouponInfoResponseVo> responseVos = new ArrayList<>();
        Page<UserCoupon> page = new Page<>(pageNum,pageSize);
        //查看可使用的
        if(screenCondition==0){
            IPage<UserCoupon> couponPage = userCouponMapper.selectPage(page,new QueryWrapper<UserCoupon>().eq("customer_id",customerId).eq("status",0));
            List<UserCoupon> userCoupons = couponPage.getRecords();
            for (UserCoupon userCoupon:userCoupons) {
                CouponInfoResponseVo responseVo=new CouponInfoResponseVo();
                Coupon coupon=couponMapper.selectById(userCoupon.getCouponId());
                responseVo.setId(userCoupon.getCouponId());
                responseVo.setStoreId(coupon.getStoreId());
                responseVo.setTargetPrice(coupon.getTargetPrice());
                responseVo.setDiscount(coupon.getDiscount());
                responseVo.setGmtCreate(coupon.getGmtCreate());
                responseVo.setStartTime(coupon.getStartTime());
                responseVo.setDeadline(coupon.getDeadline());
                responseVos.add(responseVo);
            }
            PageResponseVo<CouponInfoResponseVo> couponInfoResponsePage=new PageResponseVo(couponPage);
            couponInfoResponsePage.setData(responseVos);
            return couponInfoResponsePage;
        }
        //查看已使用的
        else {
            IPage<UserCoupon> couponPage = userCouponMapper.selectPage(page,new QueryWrapper<UserCoupon>().eq("customer_id",customerId).eq("status",1));
            List<UserCoupon> userCoupons = couponPage.getRecords();
            for (UserCoupon userCoupon:userCoupons) {
                CouponInfoResponseVo responseVo=new CouponInfoResponseVo();
                Coupon coupon=couponMapper.selectById(userCoupon.getCouponId());
                responseVo.setId(userCoupon.getCouponId());
                responseVo.setStoreId(coupon.getStoreId());
                responseVo.setTargetPrice(coupon.getTargetPrice());
                responseVo.setDiscount(coupon.getDiscount());
                responseVo.setGmtCreate(coupon.getGmtCreate());
                responseVo.setStartTime(coupon.getStartTime());
                responseVo.setDeadline(coupon.getDeadline());
                responseVos.add(responseVo);
            }
            PageResponseVo<CouponInfoResponseVo> couponInfoResponsePage=new PageResponseVo(couponPage);
            couponInfoResponsePage.setData(responseVos);
            return couponInfoResponsePage;
        }
    }

    //用户使用优惠券
    @Override
    public void updateCouponStatus(Integer customerId, Integer couponId, Integer status) {
        UpdateWrapper<UserCoupon> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("customer_id",customerId).eq("coupon_id",couponId).set("status",status);
        userCouponMapper.update(null,updateWrapper);
    }
}
