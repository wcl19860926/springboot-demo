package com.study.oauth2.server.config.local;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class LocalcConfiguration {

    @Bean(name = "localeResolver")  //必须name=localeResolver
    public LocaleResolver gatewayLocaleResolver() {
        return new AuthServerLocaleResolver();
    }
}
