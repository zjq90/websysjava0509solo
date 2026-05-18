package com.heritage.entity;

import com.heritage.enums.AuditStatus;
import com.heritage.enums.RiskLevel;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "heritage")
public class Heritage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String dynasty;

    @Column(length = 100)
    private String material;

    @Column(length = 100)
    private String usageType;

    @Column(precision = 15, scale = 2)
    private BigDecimal estimatedValue;

    @Column(length = 255)
    private String origin;

    @Column(length = 50)
    private String province;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String verificationCode;

    @Column(columnDefinition = "TEXT")
    private String images;

    @Column(length = 200)
    private String ownerName;

    @Column(length = 18)
    private String ownerIdCard;

    @Column(length = 20)
    private String ownerPhone;

    @Column(length = 100)
    private String provenance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RiskLevel riskLevel = RiskLevel.LOW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuditStatus auditStatus = AuditStatus.PENDING;

    @Column(length = 500)
    private String auditRemark;

    private Long auditorId;

    private LocalDateTime auditTime;

    @Column(nullable = false)
    private Boolean apiVerified = false;

    @Column(length = 500)
    private String apiVerifyResult;

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
