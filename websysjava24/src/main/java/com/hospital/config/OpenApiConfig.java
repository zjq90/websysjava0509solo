package com.hospital.config;

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
                        .title("医院药品与物资管理系统 API")
                        .version("1.0.0")
                        .description("医院药品及各类物资耗材全生命周期管理系统接口文档")
                        .contact(new Contact()
                                .name("医院管理系统开发团队")
                                .email("support@hospital.com")));
    }
}
