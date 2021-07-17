package com.stack.dogcat.gomall.order.controller;

import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.service.impl.StatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/17 16:27
 * @Descrition 处理店铺的统计信息相关请求
 */
@RestController
@RequestMapping("/order/statistics")
public class StatisticsController {

    @Autowired
    StatisticsServiceImpl statisticsService;

    /**
     * 商家按年份查看每月的销售额
     * @param storeId
     * @param year
     * @return
     */
    @GetMapping("/listTurnOverByYear")
    public SysResult listTurnOverByYear(Integer storeId, String year) {
        List<BigDecimal> turnovers;
        try {
            turnovers = statisticsService.listTurnOverByYear(storeId, year);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("年销售额统计信息获取失败");
        }
        return SysResult.success(turnovers);
    }

}
