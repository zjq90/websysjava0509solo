package com.bike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 共享单车服务端应用入口
 * 
 * @author bike-sharing
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BikeSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeSharingApplication.class, args);
    }
}
