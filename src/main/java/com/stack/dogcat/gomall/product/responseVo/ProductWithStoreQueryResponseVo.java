package com.stack.dogcat.gomall.product.responseVo;

/**
 * @Author Yang Jie
 * @Date 2021/7/16 17:25
 * @Descrition TODO
 */
public class ProductWithStoreQueryResponseVo {

    private ProductQueryResponseVo product;

    private StoreQueryResponseVo store;

    @Override
    public String toString() {
        return "ProductWithStoreQueryResponseVo{" +
                "product=" + product +
                ", store=" + store +
                '}';
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
