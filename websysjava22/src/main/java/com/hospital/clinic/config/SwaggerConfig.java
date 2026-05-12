package com.hospital.clinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger OpenAPI配置类
 * 配置API文档的基本信息
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("门诊管理系统 API")
                        .version("1.0.0")
                        .description("医院门诊管理系统接口文档，包括预约挂号、分诊叫号、医生工作站、门诊收费、药房管理等功能")
                        .contact(new Contact()
                                .name("医院信息科")
                                .email("support@hospital.com")));
    }
}
