package com.onlybuns.onlybuns.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@TestConfiguration
@Profile("test")
public class TestRedisConfiguration {
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        return mock(RedisConnectionFactory.class);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        @SuppressWarnings("unchecked")
        RedisTemplate<String, Object> mockTemplate = mock(RedisTemplate.class);
        mockTemplate.setConnectionFactory(redisConnectionFactory);
        return mockTemplate;
    }
}