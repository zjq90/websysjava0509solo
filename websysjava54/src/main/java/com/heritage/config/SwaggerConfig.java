package com.heritage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("文物收藏管理后台系统 API")
                        .version("1.0.0")
                        .description("文物收藏管理后台系统，包含文物管理、用户管理、交易监管、数据看板等功能")
                        .contact(new Contact()
                                .name("Heritage Team")
                                .email("support@heritage.com")));
    }
}
