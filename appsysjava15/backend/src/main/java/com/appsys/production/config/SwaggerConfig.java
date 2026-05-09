package com.appsys.production.config;

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
                        .title("生产批次管理系统 API")
                        .version("1.0.0")
                        .description("生产批次管理系统后端RESTful API，支持生产批次进度追踪、扫码质检、异常预警等功能")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("support@appsys.com")));
    }
}
