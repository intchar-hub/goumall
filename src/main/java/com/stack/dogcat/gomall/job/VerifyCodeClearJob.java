package com.stack.dogcat.gomall.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stack.dogcat.gomall.user.mapper.VerifyCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author Yang Jie
 * @Date 2021/7/21 9:33
 * @Descrition TODO
 */
@Component
public class VerifyCodeClearJob {

    @Autowired
    VerifyCodeMapper verifyCodeMapper;

    @Scheduled(cron= "0 0/5 * * * ? ")
    public void checkCouponTime(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.lt("gmt_create", LocalDateTime.now().minusMinutes(5));
        verifyCodeMapper.delete(queryWrapper);
    }

}
