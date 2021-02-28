package com.study.common.core.config;

import com.google.common.base.Predicate;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


public abstract class AbstractSwaggerConfig {
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(basePackages(new String[]{getBasePackage()}))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(getTitle())
                .description(getDescription())
                .version(getVersion())
                .build();
    }

    protected abstract String getBasePackage();

    protected abstract String getTitle();

    protected abstract String getDescription();

    protected abstract String getVersion();

    private Predicate<RequestHandler> basePackages(final String[] basePackages) {
        return input -> {
            if (input != null && basePackages != null && basePackages.length > 0) {
                Class<?> cls = input.getHandlerMethod().getMethod().getDeclaringClass();
                String clsName = cls.getPackage().getName();
                for (String pkgName : basePackages) {
                    if (clsName.startsWith(pkgName)) {
                        return true;
                    }
                }
            }
            return false;
        };
    }
}
