package com.seedtrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 种子质量追溯系统 - 主应用启动类
 * 
 * <p>这是Spring Boot应用的入口类，负责启动整个应用。
 * 系统采用分层架构，包含以下模块：</p>
 * <ul>
 *   <li>controller - REST API控制器层</li>
 *   <li>service - 业务逻辑层</li>
 *   <li>repository - 数据访问层</li>
 *   <li>entity - 实体模型层</li>
 *   <li>dto - 数据传输对象</li>
 *   <li>config - 配置类</li>
 *   <li>security - 安全加密组件</li>
 *   <li>exception - 异常处理</li>
 *   <li>util - 工具类</li>
 * </ul>
 * 
 * <p>技术栈：
 * <ul>
 *   <li>Spring Boot 2.7.18</li>
 *   <li>Spring Data JPA</li>
 *   <li>H2 内存数据库</li>
 *   <li>SpringDoc OpenAPI (Swagger)</li>
 *   <li>iText 7 (PDF生成)</li>
 *   <li>AES-256 加密</li>
 * </ul>
 * </p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@SpringBootApplication
@EntityScan(basePackages = "com.seedtrace.entity")
@EnableJpaRepositories(basePackages = "com.seedtrace.repository")
public class SeedTraceApplication {

    /**
     * 主方法 - 应用入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SeedTraceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  种子质量追溯系统启动成功！");
        System.out.println("========================================");
        System.out.println("访问地址：");
        System.out.println("  API文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("  基础URL: http://localhost:8080/api");
        System.out.println("========================================\n");
    }
}
