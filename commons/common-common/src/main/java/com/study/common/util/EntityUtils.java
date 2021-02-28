package com.study.common.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class EntityUtils {

    private EntityUtils() {

    }


    public static <E, R> List<R> applyProperty(List<E> entityList, Function<E, R> function) {
        List<R> integerPropertyList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            entityList.forEach(item -> {
                integerPropertyList.add(function.apply(item));
            });
        }
        return integerPropertyList;
    }


    public static <E, R> Map<R, E> getEntityMap(List<E> entityList, Function<E, R> function) {
        Map<R, E> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            entityList.forEach(item -> {
                map.put(function.apply(item), item);
            });
        }
        return map;
    }


    public static <E, K, V> Map<K, V> getEntityMap(List<E> entityList, Function<E, K> keyFunction, Function<E, V> valueFunction) {
        Map<K, V> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            entityList.forEach(item -> {
                map.put(keyFunction.apply(item), valueFunction.apply(item));
            });
        }
        return map;
    }


}
