package com.flower.config;

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
                        .title("花店APP系统API文档")
                        .version("1.0.0")
                        .description("花店APP系统后端接口文档，包含用户、商品、订单、活动等模块")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@flower.com")));
    }
}