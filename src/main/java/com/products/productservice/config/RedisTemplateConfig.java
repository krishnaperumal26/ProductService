package com.products.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Configuration class for setting up Redis-related beans.
 * Provides a centralized configuration for RedisTemplate.
 */
@Configuration
public class RedisTemplateConfig {

    /**
     * Creates and configures a {@link RedisTemplate} bean.
     * The {@link RedisTemplate} is used for interacting with Redis data structures.
     *
     * @param redisConnectionFactory The factory for creating Redis connections.
     * @return A configured instance of {@link RedisTemplate} for String keys and Object values.
     */
    @Bean
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // Create a new RedisTemplate instance
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // Set the connection factory for the RedisTemplate
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // Return the configured RedisTemplate
        return redisTemplate;
    }
}