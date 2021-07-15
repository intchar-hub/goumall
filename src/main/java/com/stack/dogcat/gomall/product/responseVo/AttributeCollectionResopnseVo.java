package com.stack.dogcat.gomall.product.responseVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttributeCollectionResopnseVo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 属性集合名
     */
    private String name;

    /**
     * 属性集合包含的属性数量
     */
    private Integer attributeNum;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

}
