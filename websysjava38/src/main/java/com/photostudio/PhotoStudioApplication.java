package com.photostudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 影楼管理系统主启动类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@SpringBootApplication
public class PhotoStudioApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(PhotoStudioApplication.class, args);
        System.out.println("========================================");
        System.out.println("  影楼web平台管理系统启动成功!");
        System.out.println("  后端API: http://localhost:8080/api/");
        System.out.println("  Swagger文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("========================================");
    }

    /**
     * 配置跨域请求
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
