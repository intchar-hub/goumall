package com.stack.dogcat.gomall.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.sales.entity.SalesPromotion;
import com.stack.dogcat.gomall.sales.mapper.SalesPromotionMapper;
import com.stack.dogcat.gomall.sales.requestVo.SalesPromotionSaveRequestVo;
import com.stack.dogcat.gomall.sales.responseVo.SalesProductQueryResponseVo;
import com.stack.dogcat.gomall.sales.responseVo.SalesPromotionQueryResponseVo;
import com.stack.dogcat.gomall.sales.service.ISalesPromotionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.user.service.impl.StoreServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@Service
public class SalesPromotionServiceImpl extends ServiceImpl<SalesPromotionMapper, SalesPromotion> implements ISalesPromotionService {

    private static final Logger LOG = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    SalesPromotionMapper salesPromotionMapper;

    @Autowired
    ProductMapper productMapper;

    /**
     * 发布秒杀活动
     * @param requestVo
     */
    @Override
    @Transactional
    public void savePromotion(SalesPromotionSaveRequestVo requestVo) {

        //判断活动起止时间是否合理
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = requestVo.getStartTime();
        LocalDateTime deadline = requestVo.getDeadline();
        if(deadline.isBefore(now) || startTime.isBefore(now)
                || startTime.isAfter(deadline) || startTime.isEqual(deadline)) {
            LOG.error("活动起止时间不合理");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        queryWrapper.gt("deadline", requestVo.getDeadline());
        queryWrapper.lt("start_time", requestVo.getDeadline());
        List<SalesPromotion> salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        if(salesPromotionsDB != null || salesPromotionsDB.size() > 0) {
            LOG.info("该商品在该时间段内已参加秒杀活动");
            throw new RuntimeException();
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        queryWrapper.gt("deadline", requestVo.getStartTime());
        queryWrapper.lt("start_time", requestVo.getStartTime());
        salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        if(salesPromotionsDB != null || salesPromotionsDB.size() > 0) {
            LOG.info("该商品在该时间段内已参加秒杀活动");
            throw new RuntimeException();
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        queryWrapper.gt("start_time", requestVo.getStartTime());
        queryWrapper.lt("deadline", requestVo.getDeadline());
        salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        if(salesPromotionsDB != null || salesPromotionsDB.size() > 0) {
            LOG.info("该商品在该时间段内已参加秒杀活动");
            throw new RuntimeException();
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        queryWrapper.lt("start_time", requestVo.getStartTime());
        queryWrapper.gt("deadline", requestVo.getDeadline());
        salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        if(salesPromotionsDB != null || salesPromotionsDB.size() > 0) {
            LOG.info("该商品在该时间段内已参加秒杀活动");
            throw new RuntimeException();
        }

        /**
         * 秒杀活动表
         */
        SalesPromotion salesPromotion = CopyUtil.copy(requestVo, SalesPromotion.class);
        salesPromotion.setGmtCreate(LocalDateTime.now());
        salesPromotionMapper.insert(salesPromotion);

        /**
         * 商品表
         */
        Product productDB = productMapper.selectById(requestVo.getProductId());
        productDB.setIsOnsale(1);
        productMapper.updateById(productDB);

    }

    /**
     * 商家查看店铺内秒杀活动
     * @param storeId
     * @param screenCondition
     * @return
     */
    @Override
    public PageResponseVo<SalesPromotionQueryResponseVo> listPromotionByStore(Integer pageNum, Integer pageSize, Integer storeId, Integer screenCondition) {
        if(pageNum == null || pageSize == null || storeId == null || screenCondition == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", storeId);
        LocalDateTime now = LocalDateTime.now();
        if (screenCondition < 0 || screenCondition > 2) {
            LOG.error("筛选条件有误");
            throw new RuntimeException();
        }
        if(screenCondition == 1) { //1：查询已失效
//            queryWrapper.lt("deadline", now);
            queryWrapper.eq("status", 1);
        } else if(screenCondition == 2) { //2：查询未失效
//            queryWrapper.gt("deadline", now);
            queryWrapper.eq("status", 0);
        }

        Page<SalesPromotion> page = new Page(pageNum,pageSize);
        IPage<SalesPromotion> salesPromotionIPage = salesPromotionMapper.selectPage(page, queryWrapper);
        PageResponseVo<SalesPromotionQueryResponseVo> pageResponseVo = new PageResponseVo(salesPromotionIPage);
        List<SalesPromotionQueryResponseVo> vos = new ArrayList<>();

        for (SalesPromotion record : salesPromotionIPage.getRecords()) {
            Product productDB = productMapper.selectById(record.getProductId());

            SalesPromotionQueryResponseVo vo = CopyUtil.copy(record, SalesPromotionQueryResponseVo.class);
            vo.setProductName(productDB.getName());
            vo.setImagePath(productDB.getImagePath());

            vos.add(vo);
        }

        pageResponseVo.setData(vos);

        return pageResponseVo;
    }

    /**
     * 顾客查看秒杀活动
     * @param screenCondition
     * @return
     */
    @Override
    public List<SalesPromotionQueryResponseVo> listPromotion(Integer screenCondition) {
        if(screenCondition == null) {
            LOG.error("缺少请求参数");
            throw new RuntimeException();
        }

        List<SalesPromotion> salesPromotionsDB = salesPromotionMapper.selectList(null);
        List<SalesPromotionQueryResponseVo> responseVos = CopyUtil.copyList(salesPromotionsDB, SalesPromotionQueryResponseVo.class);
        LocalDateTime now = LocalDateTime.now();
        if (screenCondition < 0 || screenCondition > 3) {
            LOG.error("筛选条件有误");
            throw new RuntimeException();
        }
        List<SalesPromotionQueryResponseVo> filterList = new ArrayList<>();
        if(screenCondition == 1) { //1：查询已失效，则剔除未失效
            for (SalesPromotionQueryResponseVo vo : responseVos) {
                if(vo.getDeadline().isAfter(now)) {
                    filterList.add(vo);
                }
            }
            responseVos.removeAll(filterList);
        } else if(screenCondition == 2) { //2：查询进行中
            for (SalesPromotionQueryResponseVo vo : responseVos) {
                if(!(vo.getStartTime().isBefore(now) && vo.getDeadline().isAfter(now))) {
                    filterList.add(vo);
                }
            }
            responseVos.removeAll(filterList);
        } else if(screenCondition == 3) { //3：查询即将开始（一天）
            for (SalesPromotionQueryResponseVo vo : responseVos) {
                if(!(vo.getStartTime().isAfter(now)
                        && (vo.getStartTime().minusDays(1).isBefore(now) || vo.getStartTime().minusDays(1).isEqual(now)))) {
                    filterList.add(vo);
                }
            }
            responseVos.removeAll(filterList);
        }
        return responseVos;
    }

    /**
     * 顾客查看所有参与秒杀活动的商品
     * @return
     */
    @Override
    public List<SalesProductQueryResponseVo> listPromotionProducts() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_onsale", 1);
        queryWrapper.eq("status", 1);
        List<Product> productsDB = productMapper.selectList(queryWrapper);
        List<SalesProductQueryResponseVo> responseVos = new ArrayList<>();
        for (Product product : productsDB) {
            SalesProductQueryResponseVo vo = new SalesProductQueryResponseVo();
            vo.setProductId(product.getId());
            vo.setProductImagepath(product.getImagePath());

            responseVos.add(vo);
        }
        return responseVos;
    }
}
