package com.study.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class BeanUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	/**
	 * 获取bean.
	 *
	 * @param cls bean class type
	 * @return bean
	 */
	public static <T> T getBean(Class<T> cls) {
		try {
			if (applicationContext == null || cls == null) {
				return null;
			}
			return applicationContext.getBean(cls);
		} catch (Exception ex) {
			throw new RuntimeException("error occurred when get bean " + cls.getName(), ex);
		}
	}

	/**
	 * @param beanName --bean Name
	 * @return bean
	 */
	public static Object getBean(String beanName) {
		if (applicationContext == null || beanName == null || beanName.length() == 0) {
			return null;
		}
		return applicationContext.getBean(beanName);
	}

	/**
	 * @param clsName     --class Name
	 * @param classloader --类加载器
	 * @return bean
	 */
	public static Object getBean(String clsName, ClassLoader classloader) {
		try {
			if (applicationContext == null || clsName == null || clsName.length() == 0) {
				return null;
			}

			Class<?> cls = (classloader == null ? Class.forName(clsName) : Class.forName(clsName, false, classloader));
			return getBean(cls);
		} catch (Exception ex) {
			throw new RuntimeException("error occurred when get bean " + clsName, ex);
		}
	}

	/**
	 * 通过类型获取beans
	 *
	 * @param cls
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getBeansByType(Class<T> cls) {
		Map<String, T> map = applicationContext.getBeansOfType(cls);
		List<T> beans = new ArrayList<>();
		if (map != null && map.size() > 0) {
			beans.addAll(map.values());
		}
		return beans;
	}

	/**
	 * 获取当前profile
	 *
	 * @return profiles
	 */
	public static String[] getActiveProfiles() {
		if (applicationContext != null) {
			return applicationContext.getEnvironment().getActiveProfiles();
		}
		return new String[0];
	}
}
