package com.microservices.data.cache.redis;

import com.microservices.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class RedisPool {

    private Logger logger = LoggerFactory.getLogger(RedisPool.class);

    @Value("${data.redis.host}")
    private  String host ;

    @Value("${data.redis.port}")
    private  int port ;

    @Value("${data.redis.auth}")
    private  String auth ;

    @Value("${data.redis.minIdle}")
    private  int minIdle;

    @Value("${data.redis.maxIdle}")
    private  int maxIdle;

    @Value("${data.redis.maxTotal}")
    private  int maxTotal;

    @Value("${data.redis.timeout}")
    private  int timeout;

    @Bean
    public JedisPool redisPoolFactory() {
        logger.info("JedisPool注入成功！！\n redis地址：" + host + ":" + port);

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(timeout);
        config.setTestOnBorrow(true);

        if (StringUtil.isEmpty(auth)) {
            return new JedisPool(config, host, port, timeout);
        } else {
            return new JedisPool(config, host, port, timeout, auth);
        }
    }
}
