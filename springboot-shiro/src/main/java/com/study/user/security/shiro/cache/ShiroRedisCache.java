package com.study.user.security.shiro.cache;


import com.study.common.core.redis.RedisCacheService;
import com.study.common.core.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.*;

/**
 * @author : zhaoxuan
 * @date : 2020/3/18
 */
@Slf4j
public class ShiroRedisCache<K, V> implements Cache<K, V> {
    private String shiroCacheKey;
    private static RedisCacheService redisCacheService;
    private static Object lock = new Object();


    private static RedisCacheService getRedisCacheService() {
        if (redisCacheService == null) {
            synchronized (lock) {
                if (redisCacheService == null) {
                    redisCacheService = BeanUtils.getBean(RedisCacheService.class);
                }
            }
        }
        return redisCacheService;
    }

    ShiroRedisCache(String shiroCacheKey) {
        this.shiroCacheKey = shiroCacheKey;
    }

    @Override
    public Object get(Object k) throws CacheException {
        return getRedisCacheService().getHash(shiroCacheKey, k.toString());
    }

    @Override
    public Object put(Object k, Object v) throws CacheException {
        getRedisCacheService().setHash(shiroCacheKey, k.toString(), v);
        return v;
    }

    @Override
    public Object remove(Object k) throws CacheException {
        Object obj = getRedisCacheService().deleteHash(shiroCacheKey, k.toString());
        getRedisCacheService().deleteHash(shiroCacheKey, k.toString());
        return obj;
    }

    @Override
    public void clear() throws CacheException {
        getRedisCacheService().delete(shiroCacheKey);
    }

    @Override
    public int size() {
        Set<Object> hashKeys = getRedisCacheService().getHashKeys(shiroCacheKey);
        return hashKeys.size();
    }

    @Override
    public Set<K> keys() {
        Set<Object> valueKeys = getRedisCacheService().getHashKeys(shiroCacheKey);
        Set keys = new HashSet();
        valueKeys.stream().forEach(keys::add);
        return keys;
    }

    @Override
    public Collection<V> values() {
        try {
            List<Object> hashValues = getRedisCacheService().getHashValues(shiroCacheKey);
            Collection values = new ArrayList<>();
            values.stream().forEach(values::add);
            return values;
        } catch (Exception e) {
            throw e;
        }
    }
}

