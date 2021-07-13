package com.stack.dogcat.gomall;

import com.stack.dogcat.gomall.utils.CurrentUserHandlerMethodArgReslover;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;


import java.util.List;

@SpringBootApplication
@MapperScan("com.stack.dogcat.gomall.*.mapper")
public class GomallApplication {

    public static void main(String[] args) {
        SpringApplication.run(GomallApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }







}
