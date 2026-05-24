package com.bikeshare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API文档配置类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("共享单车管理后台 API")
                        .version("1.0.0")
                        .description("共享单车管理后台系统接口文档，包含数据看板、用户管理、财务管理等模块")
                        .contact(new Contact()
                                .name("BikeShare Team")
                                .email("support@bikeshare.com")));
    }
}
