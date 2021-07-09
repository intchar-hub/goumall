package com.stack.dogcat.gomall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.stack.dogcat.gomall.*.mapper")
public class GomallApplication {

    public static void main(String[] args) {
        SpringApplication.run(GomallApplication.class, args);
    }

}
