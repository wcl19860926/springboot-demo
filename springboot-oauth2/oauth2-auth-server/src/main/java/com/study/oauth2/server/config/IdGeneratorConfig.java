package com.study.oauth2.server.config;


import com.study.oauth2.server.service.id.holder.SnowflakeIdGeneratorHolder;
import com.study.oauth2.server.service.id.impl.SnowflakeIdGeneratorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {


    @Bean("snowflakeIdGeneratorService")
    public SnowflakeIdGeneratorService getSnowflakeIdGenerator() {
        return SnowflakeIdGeneratorHolder.getInstance();
    }


}
