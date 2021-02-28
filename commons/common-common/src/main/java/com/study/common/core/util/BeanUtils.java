package com.study.common.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.Map;

@Configuration
public class BeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public BeanUtils() {
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T getBean(Class<T> cls) {
        try {
            return applicationContext != null && cls != null ? applicationContext.getBean(cls) : null;
        } catch (Exception var2) {
            throw new RuntimeException("error occurred when get bean " + cls.getName(), var2);
        }
    }

    public static Object getBean(String beanName) {
        return applicationContext != null && beanName != null && beanName.length() != 0 ? applicationContext.getBean(beanName) : null;
    }




    public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> cls) {
        return applicationContext.getBeanNamesForAnnotation(cls);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> cls) {
        return applicationContext.getBeansWithAnnotation(cls);
    }

    public static String[] getActiveProfiles() {
        return applicationContext != null ? applicationContext.getEnvironment().getActiveProfiles() : new String[0];
    }
}