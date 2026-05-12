package com.hospital.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * 医院管理系统启动类
 * 统计分析与决策支持系统
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "医院统计分析与决策支持系统API", version = "1.0", description = "医院运营指标、医疗质量、成本效益分析系统接口文档"))
public class HospitalManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }
}
