package com.stack.dogcat.gomall.order.controller;

import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.responseVo.BasicStatisticsInfoQueryResponseVo;
import com.stack.dogcat.gomall.order.responseVo.OrderNumAndIncomeQueryResponseVo;
import com.stack.dogcat.gomall.order.service.impl.StatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yang Jie
 * @Date 2021/7/17 16:27
 * @Descrition 处理店铺的统计信息相关请求
 */
@RestController
@CrossOrigin
@RequestMapping("/order/statistics")
public class StatisticsController {

    @Autowired
    StatisticsServiceImpl statisticsService;

    /**
     * 商家按月份查看每天的销售额
     * @param storeId
     * @param month
     * @return
     */
    @GetMapping("/listOrderNumAndIncomeByMonth")
    public SysResult listOrderNumAndIncomeByMonth(Integer storeId, String month) {
        List<OrderNumAndIncomeQueryResponseVo> responseVos = new ArrayList<>();
        try {
            responseVos = statisticsService.listOrderNumAndIncomeByMonth(storeId, month);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("月销售额统计信息获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 商家按年份查看每月的销售额
     * @param storeId
     * @param year
     * @return
     */
    @GetMapping("/listOrderNumAndIncomeByYear")
    public SysResult listOrderNumAndIncomeByYear(Integer storeId, String year) {
        List<OrderNumAndIncomeQueryResponseVo> responseVos = new ArrayList<>();
        try {
            responseVos = statisticsService.listOrderNumAndIncomeByYear(storeId, year);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("年销售额统计信息获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 商家查看各项基本统计信息
     * @param storeId
     * @return
     */
    @GetMapping("/listBasicStatisticsInfo")
    public SysResult listBasicStatisticsInfo(Integer storeId) {
        BasicStatisticsInfoQueryResponseVo responseVo;
        try {
            responseVo = statisticsService.listBasicStatisticsInfo(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("基本统计信息获取失败");
        }
        return SysResult.success(responseVo);
    }

}
