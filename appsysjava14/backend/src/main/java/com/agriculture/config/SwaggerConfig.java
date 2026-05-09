package com.agriculture.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 3.0 配置类
 * 用于配置API文档展示和相关元信息
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI信息
     * 
     * @return OpenAPI实例
     */
    @Bean
    public OpenAPI fieldCollectionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("农业育种试验数据采集系统 API")
                        .description("提供田间数据采集、病虫害管理、气象数据对接等功能的RESTful接口")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Agriculture System Team")
                                .email("support@agriculture.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
