package com.appsys.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Swagger/OpenAPI配置类
 * 配置API文档的基本信息和界面展示
 * 兼容Spring Boot 2.7.x + springdoc-openapi 1.x
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI文档信息
     * @return OpenAPI实例
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("销售订单管理系统 API")
                        .version("1.0.0")
                        .description("销售订单管理系统后端API文档，支持客户管理、产品管理、订单管理、物流跟踪、合同管理等功能")
                        .contact(new Contact()
                                .name("appsys-team")
                                .email("support@appsys.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("本地开发服务器")
                ));
    }

    /**
     * 配置API分组 - 产品API
     */
    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("产品管理")
                .pathsToMatch("/api/products/**")
                .build();
    }

    /**
     * 配置API分组 - 客户API
     */
    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("客户管理")
                .pathsToMatch("/api/customers/**")
                .build();
    }

    /**
     * 配置API分组 - 订单API
     */
    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("订单管理")
                .pathsToMatch("/api/orders/**")
                .build();
    }

    /**
     * 配置API分组 - 物流API
     */
    @Bean
    public GroupedOpenApi logisticsApi() {
        return GroupedOpenApi.builder()
                .group("物流跟踪")
                .pathsToMatch("/api/logistics/**")
                .build();
    }

    /**
     * 配置API分组 - 测试API
     */
    @Bean
    public GroupedOpenApi testApi() {
        return GroupedOpenApi.builder()
                .group("测试接口")
                .pathsToMatch("/api/test/**")
                .build();
    }
}
