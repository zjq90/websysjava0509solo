package com.websys.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI 配置类
 * 配置API文档信息，包括标题、描述、版本、联系人等
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Web平台管理系统 API")
                        .version("1.0.0")
                        .description("Web平台管理系统后端API接口文档，包含系统用户管理、设备管理、远程控制、商品管理等模块")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@websys.com")));
    }
}
