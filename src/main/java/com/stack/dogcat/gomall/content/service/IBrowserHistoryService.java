package com.stack.dogcat.gomall.content.service;

import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.content.entity.BrowserHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stack.dogcat.gomall.content.responseVo.BrowserHistoryQueryResponseVo;

import java.util.List;

/**
 * <p>
 * 浏览历史表 服务类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
public interface IBrowserHistoryService extends IService<BrowserHistory> {

    void saveBrowserHistory(Integer customerId, Integer productId);

    PageResponseVo<BrowserHistoryQueryResponseVo> listBrowserHistoryByCustomer(Integer customerId, Integer pageNum, Integer pageSize);

    void deleteBrowserHistoryByHistoryId(Integer browserHistoryId);

    void clearBrowserHistoryByCustomer(Integer customerId);
}
