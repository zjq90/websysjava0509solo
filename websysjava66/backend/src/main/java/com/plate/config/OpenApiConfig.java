package com.plate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("车牌识别系统 API")
                        .version("1.0.0")
                        .description("车牌识别系统管理后台接口文档")
                        .contact(new Contact().name("Admin").email("admin@plate.com")));
    }
}
