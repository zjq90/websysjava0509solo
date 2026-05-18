package com.heritage.entity;

import com.heritage.enums.AuditStatus;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String transactionNo;

    @Column(nullable = false)
    private Long heritageId;

    @Column(length = 200)
    private String heritageName;

    @Column(nullable = false)
    private Long buyerId;

    @Column(length = 50)
    private String buyerName;

    @Column(nullable = false)
    private Long sellerId;

    @Column(length = 50)
    private String sellerName;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(precision = 15, scale = 2)
    private BigDecimal estimatedValue;

    @Column(nullable = false)
    private Boolean isAbnormal = false;

    @Column(length = 500)
    private String abnormalReason;

    @Column(nullable = false)
    private Boolean fundsFrozen = false;

    @Column(length = 500)
    private String freezeReason;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AuditStatus freezeAuditStatus = AuditStatus.PENDING;

    private Long freezeAuditorId;

    private LocalDateTime freezeAuditTime;

    @Column(length = 500)
    private String freezeAuditRemark;

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
