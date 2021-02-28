package com.study.core.convert;

import ch.qos.logback.classic.pattern.DateConverter;
import com.study.core.bean.BeanUtil;
import com.study.core.exception.CloneRuntimeException;
import com.study.core.exception.ConvertException;
import com.study.core.reflect.ReflectUtil;
import com.study.core.reflect.TypeUtil;
import com.study.core.util.ObjectUtil;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.URLConverter;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;



/**
 * 转换器登记中心
 * <p>
 * 将各种类型Convert对象放入登记中心，通过convert方法查找目标类型对应的转换器，将被转换对象转换之。
 * </p>
 * <p>
 * 在此类中，存放着默认转换器和自定义转换器，默认转换器是Hutool中预定义的一些转换器，自定义转换器存放用户自定的转换器。
 * </p>
 * 
 * @author Looly
 *
 */
public class ConverterRegistry {

	/** 默认类型转换器 */
	private Map<Type, Converter<?>> defaultConverterMap;
	/** 用户自定义类型转换器 */
	private Map<Type, Converter<?>> customConverterMap;

	/** 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载 */
	private static class SingletonHolder {
		/** 静态初始化器，由JVM来保证线程安全 */
		private static ConverterRegistry instance = new ConverterRegistry();
	}

	/**
	 * 获得单例的 {@link ConverterRegistry}
	 * 
	 * @return {@link ConverterRegistry}
	 */
	public static ConverterRegistry getInstance() {
		return SingletonHolder.instance;
	}

	public ConverterRegistry() {
		defaultConverter();
	}

	/**
	 * 登记自定义转换器
	 * 
	 * @param type 转换的目标类型
	 * @param converterClass 转换器类，必须有默认构造方法
	 * @return {@link ConverterRegistry}
	 */
	public ConverterRegistry putCustom(Type type, Class<? extends Converter<?>> converterClass) {
		return putCustom(type, ReflectUtil.newInstance(converterClass));
	}

	/**
	 * 登记自定义转换器
	 * 
	 * @param type 转换的目标类型
	 * @param converter 转换器
	 * @return {@link ConverterRegistry}
	 */
	public ConverterRegistry putCustom(Type type, Converter<?> converter) {
		if (null == customConverterMap) {
			synchronized (this) {
				if (null == customConverterMap) {
					customConverterMap = new ConcurrentHashMap<>();
				}
			}
		}
		customConverterMap.put(type, converter);
		return this;
	}

	/**
	 * 获得转换器<br>
	 * 
	 * @param <T> 转换的目标类型
	 * 
	 * @param type 类型
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换器
	 */
	public <T> Converter<T> getConverter(Type type, boolean isCustomFirst) {
		Converter<T> converter = null;
		if (isCustomFirst) {
			converter = this.getCustomConverter(type);
			if (null == converter) {
				converter = this.getDefaultConverter(type);
			}
		} else {
			converter = this.getDefaultConverter(type);
			if (null == converter) {
				converter = this.getCustomConverter(type);
			}
		}
		return converter;
	}

	/**
	 * 获得默认转换器
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getDefaultConverter(Type type) {
		return (null == defaultConverterMap) ? null : (Converter<T>) defaultConverterMap.get(type);
	}

	/**
	 * 获得自定义转换器
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * 
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getCustomConverter(Type type) {
		return (null == customConverterMap) ? null : (Converter<T>) customConverterMap.get(type);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	@SuppressWarnings("unchecked")
	public <T> T convert(Type type, Object value, T defaultValue, boolean isCustomFirst) throws CloneRuntimeException {
		if (null == type && null == defaultValue) {
			throw new NullPointerException("[type] and [defaultValue] are both null, we can not know what type to convert !");
		}
		if (ObjectUtil.isNull(value)) {
			return defaultValue;
		}
		if (null == type) {
			type = defaultValue.getClass();
		}
		final Class<T> rowType = (Class<T>) TypeUtil.getClass(type);


		// 标准转换器
		final Converter<T> converter = getConverter(type, isCustomFirst);
		if (null != converter) {
			return converter.convert(value, defaultValue);
		}


		// 无法转换
		throw new ConvertException("No Converter for type [{}]", rowType.getName());
	}

	/**
	 * 转换值为指定类型<br>
	 * 自定义转换器优先
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(Type type, Object value, T defaultValue) throws ConvertException {
		return convert(type, value, defaultValue, true);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @param value 值
	 * @return 转换后的值，默认为<code>null</code>
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(Type type, Object value) throws ConvertException {
		return convert(type, value, null);
	}



	/**
	 * 注册默认转换器
	 * 
	 * @return 转换器
	 */
	private ConverterRegistry defaultConverter() {
		defaultConverterMap = new ConcurrentHashMap<>();

		// 原始类型转换器
		defaultConverterMap.put(int.class, new PrimitiveConverter(int.class));
		defaultConverterMap.put(long.class, new PrimitiveConverter(long.class));
		defaultConverterMap.put(byte.class, new PrimitiveConverter(byte.class));
		defaultConverterMap.put(short.class, new PrimitiveConverter(short.class));
		defaultConverterMap.put(float.class, new PrimitiveConverter(float.class));
		defaultConverterMap.put(double.class, new PrimitiveConverter(double.class));
		defaultConverterMap.put(char.class, new PrimitiveConverter(char.class));
		defaultConverterMap.put(boolean.class, new PrimitiveConverter(boolean.class));

		// 包装类转换器
		defaultConverterMap.put(Number.class, new NumberConverter());
		defaultConverterMap.put(Integer.class, new NumberConverter(Integer.class));
		defaultConverterMap.put(AtomicInteger.class, new NumberConverter(AtomicInteger.class));// since 3.0.8
		defaultConverterMap.put(Long.class, new NumberConverter(Long.class));
		defaultConverterMap.put(AtomicLong.class, new NumberConverter(AtomicLong.class));// since 3.0.8
		defaultConverterMap.put(Byte.class, new NumberConverter(Byte.class));
		defaultConverterMap.put(Short.class, new NumberConverter(Short.class));
		defaultConverterMap.put(Float.class, new NumberConverter(Float.class));
		defaultConverterMap.put(Double.class, new NumberConverter(Double.class));
		defaultConverterMap.put(BigDecimal.class, new NumberConverter(BigDecimal.class));
		defaultConverterMap.put(BigInteger.class, new NumberConverter(BigInteger.class));





		return this;
	}
	// ----------------------------------------------------------- Private method end
}
