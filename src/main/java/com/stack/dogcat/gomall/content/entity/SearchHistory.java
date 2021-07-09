package com.stack.dogcat.gomall.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 搜索历史表
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cms_search_history")
public class SearchHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 浏览历史id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer customerId;

    /**
     * 搜索内容
     */
    private String searchContent;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 逻辑删除，0表示未删除，1表示删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;


}
