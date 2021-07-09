package com.stack.dogcat.gomall.order.service.impl;

import com.stack.dogcat.gomall.order.entity.CartItem;
import com.stack.dogcat.gomall.order.mapper.CartItemMapper;
import com.stack.dogcat.gomall.order.service.ICartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
