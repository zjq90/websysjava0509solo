package com.bike.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI(Swagger)配置类
 * 
 * @author bike-sharing
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("共享单车管理系统API")
                        .version("1.0.0")
                        .description("共享单车服务端管理系统API文档，包含车辆调度、维护、电池管理等功能")
                        .contact(new Contact()
                                .name("bike-sharing")
                                .email("support@bike.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
