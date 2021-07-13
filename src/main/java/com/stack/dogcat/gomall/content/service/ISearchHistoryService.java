package com.stack.dogcat.gomall.content.service;

import com.stack.dogcat.gomall.content.entity.SearchHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.content.responseVo.SearchHistoryQueryResponseVo;

import java.util.List;

/**
 * <p>
 * 搜索历史表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface ISearchHistoryService extends IService<SearchHistory> {

    void saveSearchHistory(Integer customerId, String content);

    void deleteSearchHistoryByHistoryId(Integer searchHistoryId);

    void clearSearchHistoryByCustomer(Integer customerId);

    List<SearchHistoryQueryResponseVo> listSearchHistoryByCustomer(Integer customerId);
}
