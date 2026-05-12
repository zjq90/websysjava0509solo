package com.hospital.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 门诊管理系统主启动类
 * 系统功能包括：预约挂号、分诊叫号、医生工作站、门诊收费、药房管理、综合查询
 */
@SpringBootApplication
public class ClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicApplication.class, args);
    }
}
