package com.secondhand.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rsa")
public class RSAConfig {

    private String privateKey;

    private String publicKey;
}
