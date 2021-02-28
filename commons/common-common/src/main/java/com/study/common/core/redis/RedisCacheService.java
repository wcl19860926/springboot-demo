package com.study.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component("redisCacheService")
public class RedisCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public Object get(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long timeout) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout cannot be less than zero.");
        }
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void setHash(String key, String value_key, Object value) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        if (StringUtils.isEmpty(value_key)) {
            throw new IllegalArgumentException("value_key cannot be empty.");
        }
        redisTemplate.opsForHash().put(key, value_key, value);
    }

    public Object getHash(String key, String value_key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        return redisTemplate.opsForHash().get(key, value_key);
    }

    public List<Object> getHashValues(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        return redisTemplate.opsForHash().values(key);
    }

    public Set<Object> getHashKeys(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        return redisTemplate.opsForHash().keys(key);
    }

    public Map<Object, Object> getHashAll(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        return redisTemplate.opsForHash().entries(key);
    }

    public Long deleteHash(String key, String... value_keys) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        if (value_keys != null && value_keys.length > 0) {
            return redisTemplate.opsForHash().delete(key, value_keys);
        }
        return 0L;
    }

    public Boolean containKey(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        return redisTemplate.hasKey(key);
    }

    public Boolean delete(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key cannot be empty.");
        }
        return redisTemplate.delete(key);
    }

    public Long delete(List<String> keys) {
        if (!CollectionUtils.isEmpty(keys)) {
            return redisTemplate.delete(keys);
        }
        return 0l;
    }

    public Set<String> keys(String key) {
        return redisTemplate.keys(key);
    }

    public Long getExpireTime(String key) {
        return redisTemplate.getExpire(key);
    }


}
