package com.stack.dogcat.gomall.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.stack.dogcat.gomall.content.entity.BrowserHistory;
import com.stack.dogcat.gomall.content.mapper.BrowserHistoryMapper;
import com.stack.dogcat.gomall.content.responseVo.BrowserHistoryQueryResponseVo;
import com.stack.dogcat.gomall.content.service.IBrowserHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 浏览历史表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class BrowserHistoryServiceImpl extends ServiceImpl<BrowserHistoryMapper, BrowserHistory> implements IBrowserHistoryService {

    @Autowired
    BrowserHistoryMapper browserHistoryMapper;

    @Autowired
    ProductMapper productMapper;

    /**
     * 保存浏览历史
     * @param customerId
     * @param productId
     */
    @Override
    @Transactional
    public void saveBrowserHistory(Integer customerId, Integer productId) {
        /**
         * 保存浏览历史
         */
        BrowserHistory browserHistory = new BrowserHistory();
        browserHistory.setCustomerId(customerId);
        browserHistory.setProductId(productId);
        browserHistory.setGmtCreate(LocalDateTime.now());
        browserHistoryMapper.insert(browserHistory);

        /**
         * 商品的点击次数 +1
         */
        Product productDB = productMapper.selectById(productId);
        productDB.setClickNum(productDB.getClickNum() + 1);
        productMapper.updateById(productDB);
    }

    /**
     * 消费者查看浏览记录
     * @param customerId
     * @return
     */
    @Override
    public List<BrowserHistoryQueryResponseVo> listBrowserHistoryByCustomer(Integer customerId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id", customerId);
        List<BrowserHistory> browserHistoriesDB = browserHistoryMapper.selectList(queryWrapper);
        if(browserHistoriesDB == null || browserHistoriesDB.isEmpty()) {
            return null;
        }
        List<BrowserHistoryQueryResponseVo> responseVos = new ArrayList<>();
        for (BrowserHistory browserHistory : browserHistoriesDB) {

            Product product = productMapper.selectById(browserHistory.getProductId());
            if(product == null) {
                throw new RuntimeException();
            }
            String productName = product.getName();

            BrowserHistoryQueryResponseVo vo = new BrowserHistoryQueryResponseVo();
            vo.setBrowserHistoryId(browserHistory.getId());
            vo.setProductId(browserHistory.getProductId());
            vo.setProductName(productName);
            vo.setGmtCreate(browserHistory.getGmtCreate());

            responseVos.add(vo);
        }
        return responseVos;
    }

    /**
     * 删除单个浏览记录
     * @param browserHistoryId
     */
    @Override
    public void deleteBrowserHistoryByHistoryId(Integer browserHistoryId) {
        browserHistoryMapper.deleteById(browserHistoryId);
    }

    /**
     * 消费者清空浏览历史
     * @param customerId
     */
    @Override
    public void clearBrowserHistoryByCustomer(Integer customerId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id", customerId);
        browserHistoryMapper.delete(queryWrapper);
    }
}
