package com.photostudio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI 配置类
 * 配置API文档的基本信息
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("影楼Web平台管理系统 API")
                        .version("1.0.0")
                        .description("影楼管理系统接口文档，包含人员排班、场地服装、快递交付等功能")
                        .contact(new Contact()
                                .name("PhotoStudio Team")
                                .email("support@photostudio.com")));
    }
}
