package com.stack.dogcat.gomall.order.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stack.dogcat.gomall.order.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/19 0:33
 * @Descrition TODO
 */
public interface StatisticsMapper extends BaseMapper<Order> {

    /**
     * 获取：供商家选择可查看销售情况的年份，即店铺中的订单的所有年份（该店铺从入驻至今）
     * @param storeId
     * @return
     */
    @Select("select distinct left(gmt_create, 4) as order_year " +
            "from oms_order " +
            "where (store_id = #{storeId} and status = 3) " +
            "group by gmt_create " +
            "order by order_year desc;")
    List<String> listOrderYears(@Param("storeId") Integer storeId);

}
