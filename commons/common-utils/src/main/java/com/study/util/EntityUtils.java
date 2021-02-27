package com.study.util;

import com.study.function.ToStringFunction;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EntityUtils <br>
 *
 * @author @author xiquee
 * @date 2018-11-09 10:16:00
 */
public class EntityUtils {

    public static final String CHAR_REGEX = "^[A-Za-z]+$";
    public static final int MIN_ORDER_LEN = 20;
    public static final String CLASS = "class";

    /**
     * 获取某字段的值 返回List(String)
     *
     * @param entities     --Entity列表
     * @param keyExtractor --属性名称
     */
    public static <E> List<String> getStringProperties(List<E> entities, ToStringFunction<? super E> keyExtractor) {
        List<String> properties = new ArrayList<>();
        if (entities != null && entities.size() > 0 && keyExtractor != null) {
            entities.forEach(entity -> properties.add(keyExtractor.applyAsString(entity)));
        }
        return properties;
    }

    /**
     * 获取某字段的值 返回List(Integer)
     *
     * @param entities     --Entity列表
     * @param keyExtractor --属性提取函数
     */
    public static <E> List<Integer> getIntegerProperties(List<E> entities, ToIntFunction<? super E> keyExtractor) {
        List<Integer> properties = new ArrayList<>();
        if (entities != null && entities.size() > 0 && keyExtractor != null) {
            entities.forEach(entity -> properties.add(keyExtractor.applyAsInt(entity)));
        }
        return properties;
    }

    /**
     * 返回Map(String,T)
     *
     * @param entities     --Entity列表
     * @param keyExtractor --属性名称
     */
    public static <E> Map<String, E> getEntityMap(List<E> entities, ToStringFunction<? super E> keyExtractor) {
        Map<String, E> map = new HashMap<>(8);
        if (entities != null && entities.size() > 0 && keyExtractor != null) {
            entities.forEach(entity -> map.put(keyExtractor.applyAsString(entity), entity));
        }
        return map;
    }

    /**
     * 返回Map(Integer,T)
     *
     * @param entities     --Entity列表
     * @param keyExtractor --属性名称
     */
    public static <E> Map<Integer, E> getEntityMap(List<E> entities, ToIntFunction<? super E> keyExtractor) {
        Map<Integer, E> map = new HashMap<>(8);
        if (entities != null && entities.size() > 0 && keyExtractor != null) {
            entities.forEach(entity -> map.put(keyExtractor.applyAsInt(entity), entity));
        }
        return map;
    }

    /**
     * 将一个Bean转成Map
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        Map<String, Object> fieldValueMap = new HashMap<>();
        if (obj != null && !"".equals(obj)) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor property : propertyDescriptors) {
                    String key = property.getName();
                    // 过滤class属性
                    if (!CLASS.equals(key)) {
                        // 得到property对应的getter方法
                        Method getter = property.getReadMethod();
                        Object value = getter.invoke(obj);
                        fieldValueMap.put(key, value);
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return fieldValueMap;
    }

    /**
     * 根据source获取对象中的空属性.
     *
     * @param source 要获取空属性的对象
     * @return 获取到的空属性的字段名
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapperImpl src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 复制属性，并且属性为非空.
     *
     * @param src    要复制的对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        if (src != null && target != null) {
            org.springframework.beans.BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
        }
    }


    public static boolean isValidFieldName(String order) {
        if (order.length() >= MIN_ORDER_LEN) {
            return false;
        }
        Pattern pattern = Pattern.compile(CHAR_REGEX);
        Matcher matcher = pattern.matcher(order);
        return matcher.matches();
    }

    /**
     * @param obj
     * @param propertyName
     * @param value
     */
    public static void setPropertyValue(Object obj, String propertyName, Object value) {
        if (obj == null || !StringUtils.hasLength(propertyName)) {
            return;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.equals(propertyName)) {
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param clazz
     * @param properties
     * @return
     */
    public static Object createEntity(Class<?> clazz, Map<String, Object> properties) {
        try {
            Object instance = clazz.newInstance();
            if (properties != null && properties.size() > 0) {
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    setPropertyValue(instance, entry.getKey(), entry.getValue());
                }
            }
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * 使用正则表达式提取中括号中的内容
     */
    public static List<String> extractMessageByRegular(String msg, String regex) {
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(msg);
        while (m.find()) {
            list.add(m.group().substring(1, m.group().length() - 1));
        }
        return list;
    }


    /**
     * 比较两个集合<T>  如果一样则返回true， 否则返回false
     */
    public static <T> Boolean compareListT(List<T> first, List<T> second) {
        if (CollectionUtils.isEmpty(first) && CollectionUtils.isNotEmpty(second)) {
            return false;
        }
        if (CollectionUtils.isEmpty(second) && CollectionUtils.isNotEmpty(first)) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(first) && CollectionUtils.isNotEmpty(second)) {
            if (first.size() != second.size()) {
                return false;
            }
            List<T> commonList = new ArrayList<>();
            for (T t1 :
                    first) {
                for (T t2 :
                        second) {
                    if (t1.equals(t2)) {
                        commonList.add(t1);
                    }
                }
            }
            if (commonList.size() == 0 || commonList.size() != first.size() || commonList.size() != second.size()) {
                return false;
            }
        }
        return true;
    }
}
