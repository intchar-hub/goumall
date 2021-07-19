package com.stack.dogcat.gomall.product.responseVo;

import com.stack.dogcat.gomall.sales.entity.Coupon;
import com.stack.dogcat.gomall.sales.responseVo.CouponInfoResponseVo;

import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/16 17:25
 * @Descrition TODO
 */
public class ProductWithStoreQueryResponseVo {

    private ProductQueryResponseVo product;

    private StoreQueryResponseVo store;

    private List<CouponInfoResponseVo> coupons;

    @Override
    public String toString() {
        return "ProductWithStoreQueryResponseVo{" +
                "product=" + product +
                ", store=" + store +
                ", coupon=" + coupons +
                '}';
    }

    public List<CouponInfoResponseVo> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponInfoResponseVo> coupons) {
        this.coupons = coupons;
    }

    public ProductQueryResponseVo getProduct() {
        return product;
    }

    public void setProduct(ProductQueryResponseVo product) {
        this.product = product;
    }

    public StoreQueryResponseVo getStore() {
        return store;
    }

    public void setStore(StoreQueryResponseVo store) {
        this.store = store;
    }
}
