package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 医院预约挂号系统主应用类
 * 
 * @author hospital
 * @version 1.0.0
 */
@SpringBootApplication
public class HospitalApplication {

    /**
     * 应用程序入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }
}
