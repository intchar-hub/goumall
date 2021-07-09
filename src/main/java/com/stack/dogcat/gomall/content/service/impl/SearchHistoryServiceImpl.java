package com.stack.dogcat.gomall.content.service.impl;

import com.stack.dogcat.gomall.content.entity.SearchHistory;
import com.stack.dogcat.gomall.content.mapper.SearchHistoryMapper;
import com.stack.dogcat.gomall.content.service.ISearchHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
