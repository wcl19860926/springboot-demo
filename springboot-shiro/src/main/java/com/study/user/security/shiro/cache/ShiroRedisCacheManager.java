package com.study.user.security.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * @author : zhaoxuan
 * @date : 2020/3/18
 */
public class ShiroRedisCacheManager implements CacheManager {

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new ShiroRedisCache<>(s);
    }
}
