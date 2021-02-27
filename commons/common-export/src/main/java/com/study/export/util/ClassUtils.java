package com.study.export.util;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

public class ClassUtils {


    public static final String SET_PREFIX = "set";
    public static final String GET_PREFIX = "get";
    public static final String IS_PREFIX = "is";

    private static final Logger LOGGER = Logger.getLogger(ClassUtils.class);


    /**
     * @param prefix
     * @param fieldName
     * @return
     */
    public static String getMethodNameForField(final String prefix, final String fieldName) {
        return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }


    /**
     * 根据属性名，获到该属性的Get方法名
     *
     * @param fieldName
     * @return
     */
    public static String getGetMethodName(final String fieldName) {
        return getMethodNameForField(GET_PREFIX, fieldName);
    }

    /**
     * 根据属性名，获到该属性的Get方法名
     *
     * @param fieldName
     * @return
     */
    public static String getSetMethodName(final String fieldName) {
        return getMethodNameForField(SET_PREFIX, fieldName);
    }


    /**
     * @param cls
     * @param methodName
     * @return
     */
    public static Method getMethodByMethodName(Class<?> cls, String methodName) {
        try {
            return cls.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            LOGGER.warn("method  " + methodName + " not  found ");
        }
        return null;
    }


    /**
     * @param cls
     * @param fieldName
     * @return
     */
    public static Method getGetMethodByFieldName(Class<?> cls, String fieldName) {
        return getMethodByMethodName(cls, getGetMethodName(fieldName));

    }


    public static Method getMethodByMethodNameAndParametrTypes(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        try {
            return cls.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            LOGGER.error("  method " + methodName + " not  found ");
        }
        return null;

    }

    /**
     * @param cls
     * @param fieldName
     * @return
     */
    public static Method getSetMethodByFieldName(Class<?> cls, String fieldName, Class<?>... parameterTypes) {
        String setMethodName = getSetMethodName(fieldName);
        try {
            return cls.getMethod(setMethodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            LOGGER.error("get  field    " + fieldName + " ,setMethod  failed !  method " + setMethodName + " not  found ");
        }

        return null;

    }


    /**
     * cls 必须有无参的构造函数
     *
     * @param cls
     * @param cls
     * @return
     */
    public static <T> T getInstanceByClass(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            LOGGER.error("get instance  of  " + cls.getClass() + "failed !");
        }
        return null;
    }


}
