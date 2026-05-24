package com.plate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlateRecognitionApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlateRecognitionApplication.class, args);
    }
}
