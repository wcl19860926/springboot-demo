package com.study.util;

import java.lang.reflect.Field;

/**
 *  反射工具 <br>
 * @author xiquee.com <br>
 * @date 2018-11-09 10:16:00
 */
public class ReflectHelper {

    /**
     * 获取obj对象fieldName的Field.
     *
     * @param obj       从什么对象中获取
     * @param fieldName 要获取的字段
     * @return 获取到的字段属性
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        if (obj == null || !StringUtils.hasLength(fieldName)) {
            return null;
        }
        return getFieldInClass(obj.getClass(), fieldName);
    }

    /**
     * 获取class中fieldName的Field.
     *
     * @param clazz     从什么对象中获取
     * @param fieldName 要获取的字段
     * @return 获取到的字段属性
     */
    public static Field getFieldInClass(Class<?> clazz, String fieldName) {
        if (clazz == null || !StringUtils.hasLength(fieldName)) {
            return null;
        }
        for (
                Class<?> superClass = clazz;
                superClass != Object.class;
                superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
            }
        }
        return null;
    }

    /**
     * 获取field的类型
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Class<?> getFieldType(Class<?> clazz, String fieldName) {
        Field field = getFieldInClass(clazz, fieldName);
        if (field != null) {
            return field.getType();
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值.
     *
     * @param obj       从什么对象中获取
     * @param fieldName 要获取的字段
     * @return 获取到的属性名字
     * @throws SecurityException        安全异常
     * @throws NoSuchFieldException     没有这样的字段异常
     * @throws IllegalArgumentException 非法参数异常
     * @throws IllegalAccessException   非法访问异常
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值.
     *
     * @param obj       要设置的对象
     * @param fieldName 要设置的对象里的成员变量
     * @param value     要设置的属性值
     * @throws SecurityException        安全异常
     * @throws NoSuchFieldException     没有这样的字段异常
     * @throws IllegalArgumentException 非法参数异常
     * @throws IllegalAccessException   非法访问异常
     */
    public static void setValueByFieldName(Object obj, String fieldName,
                                           Object value) throws SecurityException,
            NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }
}
