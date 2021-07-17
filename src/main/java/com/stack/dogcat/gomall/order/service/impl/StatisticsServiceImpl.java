package com.stack.dogcat.gomall.order.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/17 16:31
 * @Descrition 处理店铺的统计信息相关业务
 */
@Service
public class StatisticsServiceImpl {

    /**
     * 商家按年份查询每月的销售额
     * @param storeId
     * @param year
     * @return
     */
    public List<BigDecimal> listTurnOverByYear(Integer storeId, String year) {
        return null;
    }
}
