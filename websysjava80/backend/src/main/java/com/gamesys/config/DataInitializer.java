package com.gamesys.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final DataSource dataSource;

    public DataInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化数据库...");
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("schema.sql"));
            log.info("数据库表结构创建完成");
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("data.sql"));
            log.info("测试数据初始化完成");
        } catch (Exception e) {
            log.warn("数据初始化可能已执行: {}", e.getMessage());
        }
        log.info("数据库初始化完成！");
        log.info("H2控制台: http://localhost:8080/api/h2-console");
        log.info("JDBC URL: jdbc:h2:mem:gamesys");
        log.info("API文档: http://localhost:8080/api/doc.html");
    }
}
