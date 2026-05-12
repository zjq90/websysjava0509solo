package com.hospital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI配置类
 * 配置API文档的基本信息和全局设置
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI文档信息
     * 
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI hospitalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("医院住院管理系统API")
                        .description("医院住院管理系统后端接口文档，包含入院管理、医生工作站、护士工作站、出院管理、病房管理、药房管理等模块")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hospital Management System")
                                .email("support@hospital.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
