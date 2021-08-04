package com.stack.dogcat.gomall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xrm
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsRecommendProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String productIds;

    private Integer customerId;

    private LocalDateTime gmtCreate;

    private Integer isDeleted;


}
