package com.stack.dogcat.gomall.content.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.content.responseVo.BrowserHistoryQueryResponseVo;
import com.stack.dogcat.gomall.content.service.IBrowserHistoryService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 浏览历史表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/cms/browser-history")
public class BrowserHistoryController {

    @Autowired
    IBrowserHistoryService browserHistoryService;

    /**
     * 保存浏览历史
     * @param customerId
     * @param productId
     * @return
     */
    @PostMapping("/saveBrowserHistory")
    public SysResult saveBrowserHistory(String customerId, String productId) {
        try {
            browserHistoryService.saveBrowserHistory(Integer.valueOf(customerId), Integer.valueOf(productId));
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("浏览记录保存失败");
        }
        return SysResult.success();
    }

    /**
     * 消费者查看浏览记录
     * @param customerId
     * @return
     */
    @GetMapping("/listBrowserHistoryByCustomer")
    public SysResult listBrowserHistoryByCustomer(Integer customerId, Integer pageNum, Integer pageSize) {
        PageResponseVo<BrowserHistoryQueryResponseVo> responseVos = null;
        try {
            responseVos = browserHistoryService.listBrowserHistoryByCustomer(customerId, pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("浏览记录获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 删除单个浏览记录
     * @param browserHistoryId
     * @return
     */
    @DeleteMapping("/deleteBrowserHistoryByHistoryId")
    public SysResult deleteBrowserHistoryByHistoryId(Integer browserHistoryId) {
        try {
            browserHistoryService.deleteBrowserHistoryByHistoryId(browserHistoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("浏览记录删除失败");
        }
        return SysResult.success();
    }

    /**
     * 消费者清空浏览历史
     * @param customerId
     * @return
     */
    @DeleteMapping("/clearBrowserHistoryByCustomer")
    public SysResult clearBrowserHistoryByCustomer(Integer customerId) {
        try {
            browserHistoryService.clearBrowserHistoryByCustomer(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("浏览记录清空失败");
        }
        return SysResult.success();
    }

}
