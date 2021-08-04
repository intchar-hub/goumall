package com.stack.dogcat.gomall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.stack.dogcat.gomall.order.entity.CartItem;
import com.stack.dogcat.gomall.order.mapper.CartItemMapper;
import com.stack.dogcat.gomall.order.responseVo.CartItemResponseVo;
import com.stack.dogcat.gomall.order.service.ICartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.entity.Sku;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.mapper.SkuMapper;
import com.stack.dogcat.gomall.user.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements ICartItemService {

    @Autowired
    CartItemMapper cartItemMapper;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    StoreMapper storeMapper;

    @Override
    @Transactional
    public void saveCartItem(Integer customerId,Integer productId,String productAttribute,Integer productNum){

        Map<String,Object> map =new HashMap<>();
        map.put("product_id",productId);
        map.put("product_attribute",productAttribute);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.allEq(map);
        Sku sku = skuMapper.selectOne(queryWrapper);

        queryWrapper.clear();
        queryWrapper.eq("customer_id",customerId);
        queryWrapper.eq("sku_id",sku.getId());
        CartItem cartItem1 = cartItemMapper.selectOne(queryWrapper);
        if (cartItem1 != null) {
            cartItem1.setProductNum(cartItem1.getProductNum()+productNum);
            cartItemMapper.updateById(cartItem1);

        }else {
            CartItem cartItem = new CartItem();
            cartItem.setCustomerId(customerId);
            cartItem.setProductId(productId);
            cartItem.setProductNum(productNum);

            cartItem.setSkuId(sku.getId());
            cartItem.setGmtCreate(LocalDateTime.now());
            cartItemMapper.insert(cartItem);
        }

    }

    @Override
    @Transactional
    public ArrayList<CartItemResponseVo> listCartItem(Integer customerId){

        ArrayList<CartItemResponseVo>cartItemResponseVos = new ArrayList<>();

        List<CartItem> cartItemList;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id",customerId);
        cartItemList = cartItemMapper.selectList(queryWrapper);

        for(int i=0;i<cartItemList.size();i++){

            CartItemResponseVo cartItemResponseVo = new CartItemResponseVo();
            CartItem cartItem = cartItemList.get(i);
            /**CartItemId,CustomerId,GmtCreate,ProductId,SkuId,ProductNum**/
            cartItemResponseVo.setCartItemId(cartItem.getId());
            cartItemResponseVo.setCustomerId(cartItem.getCustomerId());
            cartItemResponseVo.setGmtCreate(cartItem.getGmtCreate());
            cartItemResponseVo.setProductId(cartItem.getProductId());
            cartItemResponseVo.setSkuId(cartItem.getSkuId());
            cartItemResponseVo.setProductNum(cartItem.getProductNum());
            /**storeId,storeName,productName,imagePath,sku:productAttribute,price**/
            Product product;
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("id",cartItem.getProductId());
            product=productMapper.selectOne(queryWrapper1);
            cartItemResponseVo.setProductName(product.getName());
            cartItemResponseVo.setStoreId(product.getStoreId());
            cartItemResponseVo.setImagePath(product.getImagePath());

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("id",product.getStoreId());
            cartItemResponseVo.setStoreName(storeMapper.selectOne(queryWrapper2).getStoreName());

            Sku sku;
            QueryWrapper queryWrapper3 = new QueryWrapper();
            queryWrapper3.eq("id",cartItem.getSkuId());
            sku=skuMapper.selectOne(queryWrapper3);
            cartItemResponseVo.setProductAttribute(sku.getProductAttribute());
            cartItemResponseVo.setPrice(sku.getPrice());

            cartItemResponseVos.add(cartItemResponseVo);
        }

        return cartItemResponseVos;
    }

    @Override
    public void updateCartItemProductNum(Integer cartItemId,Integer productNum){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",cartItemId);
        CartItem cartItem = cartItemMapper.selectOne(queryWrapper);
        cartItem.setProductNum(productNum);
        cartItemMapper.updateById(cartItem);

    }

    @Override
    public void deleteCartItem(ArrayList<Integer> cartItemIdList){

        for(int i=0;i<cartItemIdList.size();i++){
            cartItemMapper.deleteById(cartItemIdList.get(i));
        }

    }
}
