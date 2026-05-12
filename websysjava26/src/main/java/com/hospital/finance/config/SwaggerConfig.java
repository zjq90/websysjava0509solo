package com.hospital.finance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI配置类
 * 配置API文档信息
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("医院收费与财务管理系统 API")
                        .version("1.0.0")
                        .description("医院收费与财务管理系统接口文档，包括门诊收费、住院收费、医保接口、财务对账等功能")
                        .contact(new Contact()
                                .name("医院系统开发团队")
                                .email("dev@hospital.com")));
    }
}