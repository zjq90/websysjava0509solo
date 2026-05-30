package com.gamesys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.gamesys.mapper")
@EnableScheduling
public class GameSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameSysApplication.class, args);
    }
}
