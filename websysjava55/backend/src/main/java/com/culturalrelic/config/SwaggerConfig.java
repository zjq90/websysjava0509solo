package com.culturalrelic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("文物收藏管理系统 API")
                        .version("1.0.0")
                        .description("基于Spring Boot的文物收藏管理系统后端API文档，包含区块链节点管理、物联网设备绑定、审核规则维护、数字分身、VR博物馆、文物识别等功能")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@culturalrelic.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
