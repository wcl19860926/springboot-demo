package com.study.annotation;


import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    /**
     * 间隔多少秒
     * @return
     */
    int seconds();

    /**
     * 在间隔期间，能登录的最多次数
     * @return
     */
    int maxCount();

    /**
     * 是否需要登录
     * @return
     */
    boolean needLogin()default true;
}
