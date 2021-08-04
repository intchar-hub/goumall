package com.stack.dogcat.gomall.product.service.impl;

import com.alipay.api.domain.IpAddrLbsInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.product.entity.PmsRecommendProduct;
import com.stack.dogcat.gomall.product.entity.Product;
import com.stack.dogcat.gomall.product.mapper.PmsRecommendProductMapper;
import com.stack.dogcat.gomall.product.mapper.ProductMapper;
import com.stack.dogcat.gomall.product.responseVo.ProductQueryResponseVo;
import com.stack.dogcat.gomall.product.service.IPmsRecommendProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stack.dogcat.gomall.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xrm
 * @since 2021-08-03
 */
@Service
public class PmsRecommendProductServiceImpl extends ServiceImpl<PmsRecommendProductMapper, PmsRecommendProduct> implements IPmsRecommendProductService {

    @Autowired
    PmsRecommendProductMapper pmsRecommendProductMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    @Token.UserLoginToken
    public PageResponseVo<ProductQueryResponseVo> listRecommendProductByCustomer(Integer customerId, Integer pageNum, Integer pageSize) {
        PmsRecommendProduct pmsRecommendProduct = pmsRecommendProductMapper.selectOne(new QueryWrapper<PmsRecommendProduct>().eq("customer_id",customerId));
        List<String> productIds = Arrays.asList(pmsRecommendProduct.getProductIds().split(","));
        Page<Product> page = new Page<>(pageNum,pageSize);
        IPage<Product> total = productMapper.selectPage(page,new QueryWrapper<Product>().eq("status",1).notIn("id",productIds));
        int totalPage=(int)total.getPages()+1;
        int totalCount=(int)total.getTotal();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 1);
        queryWrapper.in("id",productIds);
        System.out.println(productIds);
        IPage<Product> productIPage=productMapper.selectPage(page,queryWrapper);
        List<Product> productList = productIPage.getRecords();
        if(productList.size()==0){
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("status", 1);
            queryWrapper1.notIn("id",productIds);
            productIPage=productMapper.selectPage(page,queryWrapper1);
            PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
            responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));
            responseVo.setTotalPage(totalPage);
            responseVo.setTotalCount(totalCount);
            return responseVo;
        }
        else{
            PageResponseVo<ProductQueryResponseVo> responseVo = new PageResponseVo(productIPage);
            responseVo.setData(CopyUtil.copyList(productIPage.getRecords(), ProductQueryResponseVo.class));
            responseVo.setTotalPage(totalPage);
            responseVo.setTotalCount(totalCount);
            return responseVo;
        }
    }
}
