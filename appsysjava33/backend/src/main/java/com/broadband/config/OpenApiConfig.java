package com.broadband.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger配置
 * 
 * @author broadband
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("宽带服务App API文档")
                        .description("宽带服务用户端App后端接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("宽带服务团队")
                                .email("support@broadband.com")));
    }
}
