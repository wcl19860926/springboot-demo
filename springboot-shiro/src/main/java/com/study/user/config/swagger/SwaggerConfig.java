package com.study.user.config.swagger;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.study.user.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户管理系统")
                .description("用户管理系统 API 1.0 操作文档")
                //服务条款网址
                  // .termsOfServiceUrl("https://blog.csdn.net/weixin_42405670")
                .version("1.0")
                .contact(new Contact("连系邮箱", "https://blog.csdn.net/weixin_42405670", "694216530@qq.com"))
                .build();
    }

}
