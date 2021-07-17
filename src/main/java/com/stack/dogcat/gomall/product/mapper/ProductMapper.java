package com.stack.dogcat.gomall.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.stack.dogcat.gomall.product.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 自定义sql+多表查询+分页查询商品，按销量倒序排列
     * 仅作示例，无实际用途
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("select pms_product.*\n" +
            "from \n" +
            "(oms_order left join pms_product on pms_product.id = oms_order.product_id)\n" +
            "group by pms_product.id\n" +
            "order by count(pms_product.id) desc")
    IPage<Product> getProductsPageBySalesNum(IPage<Product> page, QueryWrapper queryWrapper);

}
