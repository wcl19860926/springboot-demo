package com.study.oauth2.server.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/asserts/**", "/webjars/**", "/favicon.ico")
                .addResourceLocations("classpath:/static/asserts/", "classpath:/META-INF/resources/webjars/", "classpath:/static/favicon.ico")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.HOURS).cachePrivate());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/user/logout").setViewName("/logout");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

}
