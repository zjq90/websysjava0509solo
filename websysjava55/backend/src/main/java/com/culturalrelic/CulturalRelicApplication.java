package com.culturalrelic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 文物收藏管理系统启动类
 * 基于Spring Boot + JDK8 + H2数据库构建
 * 集成Swagger-OpenAPI文档
 */
@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = "com.culturalrelic.entity")
@EnableJpaRepositories(basePackages = "com.culturalrelic.repository")
public class CulturalRelicApplication {

    public static void main(String[] args) {
        SpringApplication.run(CulturalRelicApplication.class, args);
        System.out.println("========================================");
        System.out.println("  文物收藏管理后台系统启动成功！");
        System.out.println("  Swagger文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("========================================");
    }
}
