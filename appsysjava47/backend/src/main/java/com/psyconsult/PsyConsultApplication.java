package com.psyconsult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PsyConsultApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsyConsultApplication.class, args);
    }

}
