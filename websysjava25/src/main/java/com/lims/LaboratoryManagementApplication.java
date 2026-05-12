package com.lims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 检验检查管理系统主启动类
 * 实现检验、检查科室与临床科室的信息互通，提高医技科室工作效率和报告准确性
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@SpringBootApplication
public class LaboratoryManagementApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(LaboratoryManagementApplication.class, args);
        System.out.println("================================================");
        System.out.println("  检验检查管理系统启动成功！");
        System.out.println("  Swagger文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("================================================");
    }

    /**
     * 配置跨域访问，支持前端Vue应用调用后端接口
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
