package com.stack.dogcat.gomall.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class CurrentUserConfig implements WebMvcConfigurer {

    @Bean
    public CurrentUserHandlerMethodArgReslover currentUserHandlerMethodArgReslover() {
        return new CurrentUserHandlerMethodArgReslover();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        //注册@CurrentUser注解的实现类
        argumentResolvers.add(new CurrentUserHandlerMethodArgReslover());
    }
}
