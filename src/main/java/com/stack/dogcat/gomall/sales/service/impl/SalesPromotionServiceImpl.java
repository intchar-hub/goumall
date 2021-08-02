package com.stack.dogcat.gomall.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.config.RabbitmqConfig;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.sales.entity.SalesPromotion;
import com.stack.dogcat.gomall.sales.mapper.SalesPromotionMapper;
import com.stack.dogcat.gomall.sales.requestVo.SalesPromotionSaveRequestVo;
import com.stack.dogcat.gomall.sales.responseVo.SalesProductQueryResponseVo;
import com.stack.dogcat.gomall.sales.responseVo.SalesPromotionQueryResponseVo;
import com.stack.dogcat.gomall.sales.service.ISalesPromotionService;
import com.stack.dogcat.gomall.user.service.impl.StoreServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@CacheConfig(cacheNames = "sale_promotion")
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
            throw new RuntimeException("活动起止时间不合理");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        queryWrapper.gt("deadline", requestVo.getDeadline());
        queryWrapper.lt("start_time", requestVo.getDeadline());
        List<SalesPromotion> salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        if(salesPromotionsDB != null&&salesPromotionsDB.size() > 0) {
            LOG.info("该商品在该时间段内已参加秒杀活动");
            throw new RuntimeException("该商品在该时间段内已参加秒杀活动");
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        queryWrapper.gt("deadline", requestVo.getStartTime());
        queryWrapper.lt("start_time", requestVo.getStartTime());
        salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        if(salesPromotionsDB != null&&salesPromotionsDB.size() > 0) {
            LOG.info("该商品在该时间段内已参加秒杀活动");
            throw new RuntimeException("该商品在该时间段内已参加秒杀活动");
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        queryWrapper.gt("start_time", requestVo.getStartTime());
        queryWrapper.lt("deadline", requestVo.getDeadline());
        salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        if(salesPromotionsDB != null&&salesPromotionsDB.size() > 0) {
            LOG.info("该商品在该时间段内已参加秒杀活动");
            throw new RuntimeException("该商品在该时间段内已参加秒杀活动");
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", requestVo.getProductId());
        queryWrapper.lt("start_time", requestVo.getStartTime());
        queryWrapper.gt("deadline", requestVo.getDeadline());
        salesPromotionsDB = salesPromotionMapper.selectList(queryWrapper);
        if(salesPromotionsDB != null&&salesPromotionsDB.size() > 0) {
            LOG.info("该商品在该时间段内已参加秒杀活动");
            throw new RuntimeException("该商品在该时间段内已参加秒杀活动");
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
        //判断库存是否合理
        if(productDB.getStockNum()<requestVo.getPurchasingAmount()){
            LOG.info("该商品库存不够");
            throw new RuntimeException("该商品库存不够");
        }
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
        List<SalesPromotionQueryResponseVo> responseVos = new ArrayList<>();
        for (SalesPromotion record : salesPromotionsDB) {
            Product productDB = productMapper.selectById(record.getProductId());

            SalesPromotionQueryResponseVo vo = CopyUtil.copy(record, SalesPromotionQueryResponseVo.class);
            vo.setProductName(productDB.getName());
            vo.setImagePath(productDB.getImagePath());

            responseVos.add(vo);
        }

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
//    @Cacheable(key = "'listProducts'")
    public List<SalesProductQueryResponseVo> listPromotionProducts() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_onsale", 1);
        queryWrapper.eq("status", 1);
        List<Product> productsDB = productMapper.selectList(queryWrapper);
        List<SalesProductQueryResponseVo> responseVos = new ArrayList<>();
        for (Product product : productsDB) {
            SalesProductQueryResponseVo vo = new SalesProductQueryResponseVo();
            vo.setProductImagepath(product.getImagePath());
            vo.setProductId(product.getId());
            vo.setClickNum(product.getClickNum());
            vo.setDescription(product.getDescription());
            vo.setGmtCreate(product.getGmtCreate());
            vo.setHighestPrice(product.getHighestPrice());
            vo.setIsOnsale(product.getIsOnsale());
            vo.setLowestPrice(product.getLowestPrice());
            vo.setName(product.getName());
            vo.setSalesNum(product.getSalesNum());
            vo.setStatus(product.getStatus());
            vo.setStockNum(product.getStockNum());
            vo.setStoreId(product.getStoreId());
            vo.setTypeId(product.getTypeId());

            responseVos.add(vo);
        }

        for (Product product : productsDB) {
            System.out.println(product);
        }

        for (SalesProductQueryResponseVo responseVo : responseVos) {
            System.out.println(responseVo);
        }

        return responseVos;
    }


    /**
     * 顾客参加秒杀活动
     * @return
     */
    @Override
    @RabbitListener(queues = RabbitmqConfig.ORDER_QUEUE)
    public String rushSalesByProductId(Map<String,Integer>map){

        LOG.info("收到秒杀请求消息，秒杀请求用户id为：{}，商品id为：{}", map.get("customerId"), map.get("productId"));


        return "秒杀成功";
    }


    /**
     * 秒杀活动库存数量
     * @return
     */
    @Override
    public Integer hasStock(Integer productId){

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("status",0);
        queryWrapper.eq("product_id",productId);

        SalesPromotion salesPromotion = salesPromotionMapper.selectOne(queryWrapper);

        return salesPromotion.getPurchasingAmount();
    }


    /**
     * 秒杀活动后减库存
     * @return
     */
    @Override
    @RabbitListener(queues = RabbitmqConfig.STORY_QUEUE)
    public void reduceStock(Integer productId){

        LOG.info("库存消息队列收到的消息商品ID是：{}", productId);

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("status",0);
        queryWrapper.eq("product_id",productId);

        SalesPromotion salesPromotion = salesPromotionMapper.selectOne(queryWrapper);
        if(salesPromotion.getPurchasingAmount()>=0) {
            salesPromotion.setPurchasingAmount(salesPromotion.getPurchasingAmount() - 1);
            salesPromotionMapper.updateById(salesPromotion);
        }

    }


    @Autowired
    SalesPromotionServiceImpl salesPromotionService;

    /**
     * 秒杀活动
     * @return
     */
    @Override
    public String secKill(Integer customerId,Integer productId){

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.opsForValue().set(productId.toString(),salesPromotionService.hasStock(productId),60, TimeUnit.MINUTES);

        LOG.info("参加秒杀用户id为：{}，商品id为：{}", customerId, productId);

        String message = null;
        //调用redis给相应商品库存量减一
        Long decrByResult = redisTemplate.opsForValue().decrement(productId.toString());
        if (decrByResult >= 0) {
            /**
             * 说明该商品的库存量有剩余，可以进行下订单操作
             */
            LOG.info("用户：{}秒杀该商品：{}库存有余，可以进行下订单操作", customerId, productId);
            //发消息给库存消息队列，将库存数据减一
            RabbitTemplate rabbitTemplate =new RabbitTemplate();
            rabbitTemplate.convertAndSend(RabbitmqConfig.STORY_EXCHANGE, RabbitmqConfig.STORY_ROUTING_KEY, productId);

            //发消息给订单消息队列，创建订单

            Map<String,Integer>map = new HashMap<>();
            map.put("customerId",customerId);
            map.put("productId",productId);
            rabbitTemplate.convertAndSend(RabbitmqConfig.ORDER_EXCHANGE, RabbitmqConfig.ORDER_ROUTING_KEY, map);
            message = "用户" + customerId + "秒杀" + productId + "成功";
        } else {
            /**
             * 说明该商品的库存量没有剩余，直接返回秒杀失败的消息给用户
             */
            LOG.info("用户：{}秒杀时商品的库存量没有剩余,秒杀结束", productId);
            message = "用户："+ productId + "商品的库存量没有剩余,秒杀结束";
        }
        return message;

    }

}
