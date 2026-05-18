package com.heritage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 文物收藏APP启动类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
public class HeritageApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeritageApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  文物收藏APP后端服务启动成功！");
        System.out.println("  Swagger文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("===========================================");
    }
}
