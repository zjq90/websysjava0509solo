package com.hospital.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 医院收费与财务管理系统主应用类
 * 系统功能包括：门诊收费、住院收费、医保接口、财务对账
 */
@SpringBootApplication
public class HospitalFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalFinanceApplication.class, args);
    }
}