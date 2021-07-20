package com.stack.dogcat.gomall.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品收藏
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cms_product_collection")
public class ProductCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private Integer productId;

    private LocalDateTime gmtCreate;

    private Integer status;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;


}
