package com.appsys.production.entity;

import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "quality_inspection")
public class QualityInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long batchId;

    @Column(nullable = false, length = 50)
    private String stageCode;

    private Long operatorId;

    @Column(length = 50)
    private String operatorName;

    @Column(precision = 5, scale = 1)
    private BigDecimal moisture;

    @Column(precision = 5, scale = 1)
    private BigDecimal purity;

    @Column(precision = 5, scale = 1)
    private BigDecimal germinationRate;

    @Column(length = 20)
    private String result;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

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
