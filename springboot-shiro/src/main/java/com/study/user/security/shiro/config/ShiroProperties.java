package com.study.user.security.shiro.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;


@Data
@Configuration
//@ConditionalOnProperty(prefix ="shiro.login" ,value = "attemptTimes")
public class ShiroProperties {
    /**
     * 登录时最大错误次数
     */
    @Value("${shiro.login.attempt-times:3}")
    private int attemptTimes;

    /**
     * 超过最大次数后，账号锁定时间 , 默认为分钟
     */
    @Value("${shiro.login.lock-time:3600}")
    private int lockTime ;


    /**
     * 密码Hash数次数
     */
    @Value("${shiro.login.hash-times:16}")
    private int hashTimes ;



    /**
     * 密码Hash算法
     */
    @Value("${shiro.login.hash-algorithm:MD5}")
    private String hashAlgorithm ;
}
