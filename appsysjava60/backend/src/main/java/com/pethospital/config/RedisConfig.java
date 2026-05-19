package com.pethospital.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis配置类
 * 配置Redis序列化方式和缓存配置
 * 支持Redis不可用时自动降级为无缓存模式
 * 
 * @author Pet Hospital Team
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 配置RedisTemplate
     * 使用String序列化key，JSON序列化value
     * 
     * @param connectionFactory Redis连接工厂
     * @return RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置Redis缓存管理器
     * 定义各个缓存的过期时间和序列化方式
     * 
     * @param connectionFactory Redis连接工厂
     * @return CacheManager实例
     */
    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 默认缓存配置：1小时过期
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // 定义各个缓存的特殊配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 知识库搜索缓存：30分钟过期
        cacheConfigurations.put("knowledgeSearch", 
                defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // 疾病列表缓存：1小时过期
        cacheConfigurations.put("diseaseList", 
                defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // 药品列表缓存：1小时过期
        cacheConfigurations.put("medicineList", 
                defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // 案例列表缓存：1小时过期
        cacheConfigurations.put("caseList", 
                defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // 统计数据缓存：10分钟过期
        cacheConfigurations.put("statistics", 
                defaultConfig.entryTtl(Duration.ofMinutes(10)));

        try {
            // 尝试构建Redis缓存管理器
            return RedisCacheManager.builder(connectionFactory)
                    .cacheDefaults(defaultConfig)
                    .withInitialCacheConfigurations(cacheConfigurations)
                    .transactionAware()
                    .build();
        } catch (Exception e) {
            // 如果Redis不可用，返回无缓存管理器
            System.out.println("Redis不可用，使用无缓存模式: " + e.getMessage());
            return new NoOpCacheManager();
        }
    }
}
