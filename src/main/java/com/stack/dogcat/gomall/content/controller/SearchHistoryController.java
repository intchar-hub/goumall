package com.stack.dogcat.gomall.content.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.content.responseVo.SearchHistoryQueryResponseVo;
import com.stack.dogcat.gomall.content.service.ISearchHistoryService;
import com.stack.dogcat.gomall.content.service.ISearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 搜索历史表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/cms/search-history")
public class SearchHistoryController {

    @Autowired
    ISearchHistoryService searchHistoryService;

    /**
     * 保存搜索历史
     * @param customerId
     * @param content
     * @return
     */
    @PostMapping("/saveSearchHistory")
    public SysResult saveSearchHistory(String customerId, String content) {
        try {
            searchHistoryService.saveSearchHistory(Integer.valueOf(customerId), content);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("搜索记录保存失败");
        }
        return SysResult.success();
    }

    /**
     * 消费者查看搜索记录
     * @param customerId
     * @return
     */
    @GetMapping("/listSearchHistoryByCustomer")
    public SysResult listSearchHistoryByCustomer(Integer customerId) {
        List<SearchHistoryQueryResponseVo> responseVos;
        try {
            responseVos = searchHistoryService.listSearchHistoryByCustomer(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("搜索记录获取失败");
        }
        return SysResult.success(responseVos);
    }

    /**
     * 删除单个搜索记录
     * @param searchHistoryId
     * @return
     */
    @DeleteMapping("/deleteSearchHistoryByHistoryId")
    public SysResult deleteSearchHistoryByHistoryId(Integer searchHistoryId) {
        try {
            searchHistoryService.deleteSearchHistoryByHistoryId(searchHistoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("搜索记录删除失败");
        }
        return SysResult.success();
    }

    /**
     * 消费者清空搜索历史
     * @param customerId
     * @return
     */
    @DeleteMapping("/clearSearchHistoryByCustomer")
    public SysResult clearSearchHistoryByCustomer(Integer customerId) {
        try {
            searchHistoryService.clearSearchHistoryByCustomer(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.error("搜索记录清空失败");
        }
        return SysResult.success();
    }

}
