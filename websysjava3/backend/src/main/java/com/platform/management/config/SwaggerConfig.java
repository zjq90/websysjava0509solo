package com.platform.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 * 配置OpenAPI文档信息
 * 
 * @author platform
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Web平台管理系统API")
                        .version("1.0.0")
                        .description("Web平台管理系统接口文档，包含异常订单、对账管理、发票管理、销售报表等模块")
                        .contact(new Contact()
                                .name("platform")
                                .email("support@platform.com")));
    }
}
