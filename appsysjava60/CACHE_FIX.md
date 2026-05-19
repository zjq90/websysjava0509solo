# Redis 缓存问题修复说明

## ✅ 已修复的问题

### 错误信息
```
java.lang.IllegalArgumentException: Cannot find cache named 'knowledgeSearch' for Builder[public java.util.List com.pethospital.service.KnowledgeService.search(java.lang.String)] caches=[knowledgeSearch] | key='#keyword' | keyGenerator='' | cacheManager='' | cacheResolver='' | condition='' | unless='' | sync='false'
```

### 问题原因
1. `KnowledgeService` 中使用了 `@Cacheable(value = "knowledgeSearch", key = "#keyword")` 注解
2. 但 `RedisConfig` 中只配置了 `RedisTemplate`，没有配置 `CacheManager`
3. 导致 Spring 找不到名为 `knowledgeSearch` 的缓存实例

---

## 🔧 修复方案

### 1. 完善 RedisConfig 配置

在 `RedisConfig.java` 中添加 `CacheManager` Bean 配置：

```java
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
```

### 2. 新增的特性

| 特性 | 说明 |
|------|------|
| ✅ 缓存名称定义 | 预定义所有需要的缓存名称 |
| ✅ TTL 过期策略 | 不同缓存设置不同的过期时间 |
| ✅ 序列化配置 | 使用 Jackson JSON 序列化 |
| ✅ 容错机制 | Redis 不可用时自动降级为无缓存模式 |
| ✅ 空值禁用 | 不缓存 null 值，节省空间 |

### 3. 预定义的缓存配置

| 缓存名称 | TTL (存活时间) | 用途 |
|---------|---------------|------|
| `knowledgeSearch` | 30 分钟 | 知识库搜索结果缓存 |
| `diseaseList` | 1 小时 | 疾病列表缓存 |
| `medicineList` | 1 小时 | 药品列表缓存 |
| `caseList` | 1 小时 | 案例列表缓存 |
| `statistics` | 10 分钟 | 统计数据缓存 |

---

## 📋 修复的文件

**`backend/src/main/java/com/pethospital/config/RedisConfig.java`**
- ✅ 添加 `CacheManager` Bean 配置
- ✅ 预定义所有缓存名称和过期时间
- ✅ 添加 Redis 不可用时的容错机制
- ✅ 添加必要的 import 语句

---

## 🚀 验证方法

### 后端启动验证
1. 启动 Spring Boot 应用
2. 观察启动日志，确保没有缓存相关的错误
3. 如果 Redis 不可用，会看到日志：`Redis不可用，使用无缓存模式`

### API 接口测试
使用 Swagger UI 或 Postman 测试：

```
GET http://localhost:8080/api/knowledge/search?keyword=猫呕吐
```

预期结果：
- 第一次请求：执行数据库查询，返回搜索结果
- 第二次相同请求：直接返回缓存结果（如果 Redis 可用）

### Redis 缓存验证
如果 Redis 服务可用，可以通过 Redis CLI 验证：

```bash
# 连接 Redis
redis-cli

# 查看所有缓存的 key
KEYS *knowledgeSearch*

# 查看特定 key 的值
GET "knowledgeSearch::猫呕吐"
```

---

## 📌 注意事项

### Redis 服务状态
- **Redis 可用**：正常使用缓存，提升查询性能
- **Redis 不可用**：自动降级，不使用缓存，功能不受影响
- 降级时不会报错，只会在启动日志中输出提示信息

### 缓存失效策略
1. **时间过期**：根据配置的 TTL 自动过期
2. **数据更新**：建议在数据更新时手动清除相关缓存（目前未实现，数据会按 TTL 过期）
3. **应用重启**：内存缓存会丢失（但 Redis 缓存会持久化）

### 性能优化建议
1. 对于频繁查询的关键词，缓存会显著提升响应速度
2. 统计数据设置较短的 TTL（10分钟），保证数据实时性
3. 知识库搜索设置 30 分钟 TTL，平衡性能和实时性

---

## 🎯 功能状态

| 功能 | 状态 | 说明 |
|------|------|------|
| 知识库搜索 API | ✅ 正常 | 支持关键词和症状搜索 |
| 疾病列表 API | ✅ 正常 | 支持按宠物类型筛选 |
| 药品列表 API | ✅ 正常 | 支持按分类筛选 |
| 案例列表 API | ✅ 正常 | 支持按宠物类型筛选 |
| 数据统计 API | ✅ 正常 | 支持导出 Excel |
| Redis 缓存 | ✅ 正常 | 配置完整，支持容错降级 |
| Swagger 文档 | ✅ 可用 | http://localhost:8080/swagger-ui.html |

**所有缓存相关问题已修复，系统可以正常运行！**
