package com.stack.dogcat.gomall.order.service.impl;

import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.mapper.OrderMapper;
import com.stack.dogcat.gomall.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
