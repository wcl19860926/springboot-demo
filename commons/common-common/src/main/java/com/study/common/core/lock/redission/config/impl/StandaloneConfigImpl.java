package com.study.common.core.lock.redission.config.impl;



import com.study.common.core.lock.redission.RedissionProperties;
import com.study.common.core.lock.redission.config.RedissonConfigService;
import com.study.common.core.redis.constants.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * @Description: 单机部署Redisson配置
 *
 * @author xub
 * @date 2019/6/19 下午10:04
 */
@Slf4j
public class StandaloneConfigImpl implements RedissonConfigService {

    @Override
    public Config createRedissonConfig(RedissionProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String redisAddr = RedisConstants.REDIS_CONNECTION_PREFIX + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            //密码可以为空
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            log.info("初始化[单机部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("单机部署 Redisson init error", e);
        }
        return config;
    }
}
