package com.photostudio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI配置类
 * 配置API文档的基本信息
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("影楼管理系统API文档")
                        .version("1.0.0")
                        .description("影楼Web平台管理系统后端API接口文档")
                        .contact(new Contact()
                                .name("Photo Studio Team")
                                .email("support@photostudio.com")));
    }
}
