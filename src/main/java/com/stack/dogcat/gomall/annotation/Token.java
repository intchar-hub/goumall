package com.stack.dogcat.gomall.annotation;

import java.lang.annotation.*;


public @interface Token {
    //PassToken：跳过验证
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PassToken {
        boolean required() default true;
    }

    //UserLoginToken：需要验证
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserLoginToken {
        boolean required() default true;
    }

}


