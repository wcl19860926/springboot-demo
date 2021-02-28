package com.study.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Validated
@Data
public class DruidDataSourceProperties {

    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxWait;
    private Integer maxActive;
    private Long  minEvictableIdleTimeMillis;
    private String filters;

}
