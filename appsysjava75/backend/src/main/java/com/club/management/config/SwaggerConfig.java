package com.club.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI配置类
 *
 * @author club-management
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("大学生社团管理系统API")
                        .version("1.0.0")
                        .description("大学生社团管理系统后端API接口文档，包含消息通知、广场互动、经费管理、资源共享等模块")
                        .contact(new Contact()
                                .name("club-management")
                                .email("support@club.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
