package com.chatsystem.config;

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
                        .title("实时聊天系统API")
                        .version("1.0.0")
                        .description("实时聊天系统后端API接口文档")
                        .contact(new Contact()
                                .name("Chat System Team")
                                .email("support@chatsystem.com")));
    }
}
