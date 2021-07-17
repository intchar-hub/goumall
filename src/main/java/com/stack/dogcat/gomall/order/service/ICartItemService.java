package com.stack.dogcat.gomall.order.service;

import com.stack.dogcat.gomall.order.entity.CartItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.order.responseVo.CartItemResponseVo;

import java.util.ArrayList;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface ICartItemService extends IService<CartItem> {

    //加入商品到购物车
    void saveCartItem(Integer customerId,Integer productId,String productAttribute,Integer productNum);

    //根据用户id查询购物车
    ArrayList<CartItemResponseVo>listCartItem(Integer customerId);

    //更改购物车项数量
    void updateCartItemProductNum(Integer cartItemId,Integer productNum);

    //删除购物车项
    void deleteCartItem(ArrayList<Integer> cartItemIdList);
}
