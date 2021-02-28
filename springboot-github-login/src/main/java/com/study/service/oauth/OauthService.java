package com.study.service.oauth;

import com.study.cache.RedisCacheService;
import com.study.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author jitwxs
 * @since 2018/5/22 20:33
 */
@Service
public class OauthService {
    @Autowired
    private RedisCacheService RedisCacheService;


    /**
     * 生成并保存state入缓存
     *
     * @author jitwxs
     * @since 2018/5/22 20:57
     */
    public String genState() {
        String state = RandomUtils.time();
        // 保证生成的state未存在于redis中
        RedisCacheService.set(state, state, TimeUnit.SECONDS.toSeconds(60));
        return state;
    }

    /**
     * 校验state
     *
     * @author jitwxs
     * @since 2018/5/22 20:58
     */
    public Boolean checkState(String state) {
        String cacheState = (String) RedisCacheService.get(state);
        // 如果不存在，代表state非法；否则合法，并将其从缓存中删除
        return cacheState != null;
    }
}
