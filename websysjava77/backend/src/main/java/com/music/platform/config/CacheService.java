package com.music.platform.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheService {

    @Value("${cache.redis.enabled:false}")
    private boolean redisEnabled;

    private final StringRedisTemplate redisTemplate;
    private final Map<String, CacheEntry> localCache = new ConcurrentHashMap<>();

    public CacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, String value, long ttlSeconds) {
        try {
            if (redisEnabled) {
                redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
            } else {
                localCache.put(key, new CacheEntry(value, System.currentTimeMillis() + ttlSeconds * 1000));
            }
        } catch (Exception e) {
            log.warn("Redis cache set failed, using local cache: {}", e.getMessage());
            localCache.put(key, new CacheEntry(value, System.currentTimeMillis() + ttlSeconds * 1000));
        }
    }

    public void set(String key, String value) {
        set(key, value, 3600);
    }

    public Optional<String> get(String key) {
        try {
            if (redisEnabled) {
                String val = redisTemplate.opsForValue().get(key);
                return Optional.ofNullable(val);
            }
        } catch (Exception e) {
            log.warn("Redis cache get failed, using local cache: {}", e.getMessage());
        }
        CacheEntry entry = localCache.get(key);
        if (entry != null) {
            if (System.currentTimeMillis() < entry.expireTime) {
                return Optional.of(entry.value);
            }
            localCache.remove(key);
        }
        return Optional.empty();
    }

    public void delete(String key) {
        try {
            if (redisEnabled) {
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            log.warn("Redis cache delete failed: {}", e.getMessage());
        }
        localCache.remove(key);
    }

    public void increment(String key) {
        try {
            if (redisEnabled) {
                redisTemplate.opsForValue().increment(key);
            } else {
                String val = get(key).orElse("0");
                localCache.put(key, new CacheEntry(String.valueOf(Long.parseLong(val) + 1),
                        System.currentTimeMillis() + 3600 * 1000));
            }
        } catch (Exception e) {
            log.warn("Redis increment failed: {}", e.getMessage());
        }
    }

    public Set<String> getKeys(String pattern) {
        try {
            if (redisEnabled) {
                return redisTemplate.keys(pattern);
            }
        } catch (Exception e) {
            log.warn("Redis keys failed: {}", e.getMessage());
        }
        Set<String> result = new HashSet<>();
        for (String key : localCache.keySet()) {
            if (key.contains(pattern.replace("*", ""))) {
                result.add(key);
            }
        }
        return result;
    }

    private static class CacheEntry {
        String value;
        long expireTime;

        CacheEntry(String value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }
    }
}
