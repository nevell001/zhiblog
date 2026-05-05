package com.ruoyi.common.cache.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * 限流配置类
 * 
 * @author nevell
 * @since 2025-12-20
 */
@Configuration
@ConditionalOnProperty(name = "spring.redis.host")
public class RateLimitConfig {

    /**
     * 限流Lua脚本
     */
    @Bean
    public RedisScript<Long> limitScript() {
        return RedisScript.of(
            "local key = KEYS[1]\n" +
            "local count = tonumber(ARGV[1])\n" +
            "local time = tonumber(ARGV[2])\n" +
            "local current = redis.call('get', key)\n" +
            "if current == false then\n" +
            "    redis.call('set', key, 1)\n" +
            "    redis.call('expire', key, time)\n" +
            "    return 1\n" +
            "else\n" +
            "    current = tonumber(current)\n" +
            "    if current < count then\n" +
            "        redis.call('incr', key)\n" +
            "        return 1\n" +
            "    else\n" +
            "        return 0\n" +
            "    end\n" +
            "end",
            Long.class
        );
    }
}