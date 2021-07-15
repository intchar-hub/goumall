package com.stack.dogcat.gomall.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 属性名
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_attribute_name")
public class AttributeName implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 属性名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 所属的属性集合id
     */
    private Integer attributeCollectionId;

    /**
     * 检索类型 没用
     */
    private Integer selectType;

    /**
     * 属性录入方式：0->手工录入；1->从列表中选取
     */
    private Integer inputType;

    /**
     * 是否支持手动新增,0->否,1->是
     */
    private Integer handAdd;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;

    /**
     * 所属分类id 没用
     */
    private Integer typeId;


}
