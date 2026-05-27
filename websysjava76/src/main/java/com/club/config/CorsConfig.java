package com.club.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 * 允许前端Vue应用跨域访问后端API
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 配置跨域映射
     *
     * @param registry 跨域注册器
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
