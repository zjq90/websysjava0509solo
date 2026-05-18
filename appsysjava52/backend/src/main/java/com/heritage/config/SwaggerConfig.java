package com.heritage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI配置类
 * 配置API文档信息和安全认证
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI基本信息
     */
    @Bean
    public OpenAPI heritageOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("文物收藏管理系统API")
                        .description("提供文物展示、鉴定服务、溯源查询、收藏管理等功能的RESTful API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Heritage Team")
                                .email("support@heritage.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }

    /**
     * 文物展示模块API分组
     */
    @Bean
    public GroupedOpenApi heritageApi() {
        return GroupedOpenApi.builder()
                .group("文物展示")
                .pathsToMatch("/heritage/**")
                .build();
    }

    /**
     * 鉴定服务模块API分组
     */
    @Bean
    public GroupedOpenApi authenticationApi() {
        return GroupedOpenApi.builder()
                .group("鉴定服务")
                .pathsToMatch("/authentication/**")
                .build();
    }

    /**
     * 溯源查询模块API分组
     */
    @Bean
    public GroupedOpenApi traceApi() {
        return GroupedOpenApi.builder()
                .group("溯源查询")
                .pathsToMatch("/trace/**")
                .build();
    }

    /**
     * 收藏管理模块API分组
     */
    @Bean
    public GroupedOpenApi collectionApi() {
        return GroupedOpenApi.builder()
                .group("收藏管理")
                .pathsToMatch("/collection/**")
                .build();
    }

    /**
     * 用户认证模块API分组
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户认证")
                .pathsToMatch("/auth/**", "/user/**")
                .build();
    }
}