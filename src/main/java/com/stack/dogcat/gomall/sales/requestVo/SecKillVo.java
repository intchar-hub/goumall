package com.stack.dogcat.gomall.sales.requestVo;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class SecKillVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer customerId;

    private Integer productId;
}
