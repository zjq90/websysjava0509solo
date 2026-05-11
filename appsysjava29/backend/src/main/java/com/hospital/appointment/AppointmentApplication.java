package com.hospital.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 医院预约挂号系统主启动类
 * 
 * @author hospital
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class AppointmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentApplication.class, args);
    }
}
