package com.study.util;

import java.lang.reflect.Field;

public class ObjectUtils extends org.springframework.util.ObjectUtils {
    public ObjectUtils() {
    }

    public static boolean checkFieldValueNull(Object bean) {
        if (bean == null) {
            return true;
        } else {
            Class<?> cls = bean.getClass();
            Field[] fields = cls.getDeclaredFields();
            Field[] var3 = fields;
            int var4 = fields.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                field.setAccessible(true);

                try {
                    if (field.get(bean) != null) {
                        return false;
                    }
                } catch (IllegalAccessException var8) {
                    throw new RuntimeException();
                }
            }

            return true;
        }
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}