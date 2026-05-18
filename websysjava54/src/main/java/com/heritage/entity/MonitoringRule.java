package com.heritage.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "monitoring_rule")
public class MonitoringRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String ruleName;

    @Column(nullable = false, length = 50)
    private String ruleType;

    @Column(nullable = false)
    private Integer frequencyThreshold = 10;

    @Column(nullable = false)
    private Integer frequencyMinutes = 60;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal priceDeviationThreshold = new BigDecimal("50.00");

    @Column(nullable = false)
    private Integer lowCreditScoreThreshold = 60;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
