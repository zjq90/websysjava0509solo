package com.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * 运营端后台管理系统启动类
 * 
 * @author ops-admin
 * @version 1.0.0
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "运营端后台管理系统API", version = "1.0", description = "工单调度、数据分析、营销活动管理系统"))
public class OpsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpsAdminApplication.class, args);
    }
}
