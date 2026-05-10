package com.seedinventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

/**
 * Swagger OpenAPI配置类
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8080").description("本地开发服务器"));
        
        return new OpenAPI()
            .info(new Info()
                .title("种子库存管理系统API")
                .description("基于Spring Boot + H2 + Swagger的移动库存管理系统后端API文档")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Seed Inventory Team")
                    .email("support@seedinventory.com")))
            .servers(servers);
    }
}
