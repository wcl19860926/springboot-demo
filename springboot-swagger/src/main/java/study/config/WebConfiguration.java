package study.config;

import java.text.SimpleDateFormat;
import java.util.List;

import com.study.serializer.NullValueJsonSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converer : converters) {
            if (converer instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objMap = ((MappingJackson2HttpMessageConverter) converer).getObjectMapper();
                objMap.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
                objMap.getSerializerProvider().setNullValueSerializer(new NullValueJsonSerializer());

            }

        }
    }



    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/documentation/v2/api-docs", "/v2/api-docs?group=restful-api");
        registry.addRedirectViewController("/documentation/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/documentation/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
        registry.addRedirectViewController("/documentation/swagger-resources", "/swagger-resources");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/documentation/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/documentation/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


}