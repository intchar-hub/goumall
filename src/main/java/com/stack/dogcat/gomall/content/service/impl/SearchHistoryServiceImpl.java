package com.stack.dogcat.gomall.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.content.entity.SearchHistory;
import com.stack.dogcat.gomall.content.mapper.SearchHistoryMapper;
import com.stack.dogcat.gomall.content.responseVo.SearchHistoryQueryResponseVo;
import com.stack.dogcat.gomall.content.service.ISearchHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 搜索历史表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class SearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements ISearchHistoryService {

    @Autowired
    SearchHistoryMapper searchHistoryMapper;

    /**
     * 保存搜索历史
     * @param customerId
     * @param content
     * @return
     */
    @Override
    public void saveSearchHistory(Integer customerId, String content) {
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setCustomerId(customerId);
        searchHistory.setSearchContent(content);
        searchHistory.setGmtCreate(LocalDateTime.now());
        searchHistoryMapper.insert(searchHistory);
    }

    /**
     * 删除单个搜索记录
     * @param searchHistoryId
     * @return
     */
    @Override
    public void deleteSearchHistoryByHistoryId(Integer searchHistoryId) {
        searchHistoryMapper.deleteById(searchHistoryId);
    }

    /**
     * 消费者清空搜索历史
     * @param customerId
     * @return
     */
    @Override
    public void clearSearchHistoryByCustomer(Integer customerId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id", customerId);
        searchHistoryMapper.delete(queryWrapper);
    }

    /**
     * 顾客查询搜素历史
     * @param customerId
     * @return
     */
    @Override
    public List<SearchHistoryQueryResponseVo> listSearchHistoryByCustomer(Integer customerId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id", customerId);

        // 查看最近10条搜索记录
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 10");

        List<SearchHistory> searchHistoriesDB = searchHistoryMapper.selectList(queryWrapper);
        if(searchHistoriesDB == null || searchHistoriesDB.isEmpty()) {
            return null;
        }

        List<SearchHistoryQueryResponseVo> responseVos = new ArrayList<>();
        for (SearchHistory searchHistory : searchHistoriesDB) {
            SearchHistoryQueryResponseVo vo = new SearchHistoryQueryResponseVo();
            vo.setSearchHistoryId(searchHistory.getId());
            vo.setContent(searchHistory.getSearchContent());
            vo.setGmtCreate(searchHistory.getGmtCreate());

            responseVos.add(vo);
        }
        return responseVos;
    }
}
