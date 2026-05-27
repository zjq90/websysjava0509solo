package com.club.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) 配置类
 * 用于配置API文档的基本信息
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * 配置OpenAPI文档信息
     *
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI clubManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("大学生社团管理后台系统 API")
                        .description("面向校级管理员的社团管理系统接口文档，包含社团审核、活动监管、数据统计等功能")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("社团管理系统开发团队")
                                .email("support@club.edu.cn"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
