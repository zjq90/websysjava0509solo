package com.petclinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI配置类
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("宠物问诊管理后台API文档")
                        .version("1.0.0")
                        .description("宠物问诊管理后台系统接口文档，包含医院管理、用户管理、统计分析等功能")
                        .contact(new Contact()
                                .name("PetClinic Team")
                                .email("support@petclinic.com")));
    }
}
