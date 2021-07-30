package com.stack.dogcat.gomall.order.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stack.dogcat.gomall.order.entity.Order;
import com.stack.dogcat.gomall.order.responseVo.OrderNumAndIncomeQueryResponseVo;
import com.stack.dogcat.gomall.order.responseVo.ProductWithSalesNum;
import com.stack.dogcat.gomall.order.responseVo.ProductWithSalesVolume;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
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

    /**
     * 获取：店铺内各个商品名及对应的销售额
     * @param storeId
     * @return
     */
    @Select("select pms_product.name as name, sum(oms_order.total_price) as value " +
            "from " +
            "oms_order left join pms_product " +
            "on " +
            "oms_order.product_id = pms_product.id " +
            "where " +
            "(oms_order.status = 3 and oms_order.store_id = #{storeId})" +
            "group by pms_product.id;")
    List<ProductWithSalesVolume> listProductSalesVolume(@Param("storeId") Integer storeId);

    /**
     * 获取：店铺内各个商品名及对应的销售量
     * @param storeId
     * @return
     */
    @Select("select pms_product.name as name, count(oms_order.id) as value " +
            "from " +
            "oms_order left join pms_product " +
            "on " +
            "oms_order.product_id = pms_product.id " +
            "where " +
            "(oms_order.status = 3 and oms_order.store_id = #{storeId})" +
            "group by pms_product.id;")
    List<ProductWithSalesNum> listProductSalesNum(@Param("storeId") Integer storeId);

    /**
     * 获取：店铺内本周订单数
     * @param storeId
     * @return
     */
    @Select("select count(id) " +
            "from " +
            "oms_order " +
            "where " +
            "(status = 3 and store_id = #{storeId} and YEARWEEK(DATE_FORMAT(gmt_create,'%Y-%m-%d %H:%i:%s')) = YEARWEEK(NOW()));")
    Integer selectThisWeekOrderNum(@Param("storeId") Integer storeId);

    /**
     * 获取：店铺内上周订单数
     * @param storeId
     * @return
     */
    @Select("select count(id) " +
            "from " +
            "oms_order " +
            "where " +
            "(status = 3 and store_id = #{storeId} and YEARWEEK(DATE_FORMAT(gmt_create,'%Y-%m-%d %H:%i:%s')) = YEARWEEK(NOW()) - 1);")
    Integer selectLastWeekOrderNum(@Param("storeId") Integer storeId);

    /**
     * 获取：店铺内本月销售额
     * @param storeId
     * @return
     */
    @Select("select sum(total_price) " +
            "from oms_order " +
            "where " +
            "(status = 3 and store_id = #{storeId} and DATE_FORMAT(gmt_create, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m'));")
    BigDecimal selectThisMonthIncome(@Param("storeId") Integer storeId);

    /**
     * 获取：店铺内上月销售额
     * @param storeId
     * @return
     */
    @Select("select sum(total_price) " +
            "from oms_order " +
            "where " +
            "(status = 3 and store_id = #{storeId} and PERIOD_DIFF(DATE_FORMAT(NOW(),'%Y%m'),DATE_FORMAT(gmt_create,'%Y%m')) = 1);")
    BigDecimal selectLastMonthIncome(@Param("storeId") Integer storeId);

    /**
     * 获取：店铺内本周销售额
     * @param storeId
     * @return
     */
    @Select("select sum(total_price) " +
            "from oms_order " +
            "where " +
            "(status = 3 and store_id = #{storeId} and YEARWEEK(DATE_FORMAT(gmt_create,'%Y-%m-%d %H:%i:%s')) = YEARWEEK(NOW()));")
    BigDecimal selectThisWeekIncome(@Param("storeId") Integer storeId);

    /**
     * 获取：店铺内上周销售额
     * @param storeId
     * @return
     */
    @Select("select sum(total_price) " +
            "from oms_order " +
            "where " +
            "(status = 3 and store_id = #{storeId} and YEARWEEK(DATE_FORMAT(gmt_create,'%Y-%m-%d %H:%i:%s')) = YEARWEEK(NOW()) - 1);")
    BigDecimal selectLastWeekIncome(@Param("storeId") Integer storeId);

    /**
     * 按月获取：店铺内每天的销售额
     * @param storeId
     * @return
     */
    @Select("select left(gmt_create, 10) as date, sum(total_price) as order_amount, count(id) as order_count " +
            "from " +
            "oms_order " +
            "where " +
            "gmt_create like #{month} and status = 3 and store_id = #{storeId} " +
            "group by left(gmt_create, 10) " +
            "order by left(gmt_create, 10) asc;")
    List<OrderNumAndIncomeQueryResponseVo> selectOrderNumAndIncomeByMonth(@Param("storeId") Integer storeId, @Param("month") String month);

}
