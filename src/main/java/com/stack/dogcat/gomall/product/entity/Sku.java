package com.stack.dogcat.gomall.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品规格和库存量单元
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_sku")
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer productId;

    /**
     * 规格属性集合，json表示
     */
    private String productAttribute;

    /**
     * 库存量
     */
    private Integer stockNum;

    /**
     * 该规格下的商品价格
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 该规格的商品销量
     */
    private Integer salesNum;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;

}
