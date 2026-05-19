package com.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 宠物问诊管理后台系统启动类
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@SpringBootApplication
public class PetClinicApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }

    /**
     * 配置跨域访问，允许前端Vue项目访问后端API
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
