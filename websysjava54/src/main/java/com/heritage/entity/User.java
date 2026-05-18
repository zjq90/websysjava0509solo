package com.heritage.entity;

import com.heritage.enums.AuditStatus;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 50)
    private String realName;

    @Column(length = 18)
    private String idCard;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String avatar;

    @Column(columnDefinition = "TEXT")
    private String faceImage;

    @Column(nullable = false)
    private Boolean idVerified = false;

    @Column(nullable = false)
    private Boolean faceVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AuditStatus realNameAuditStatus = AuditStatus.PENDING;

    @Column(nullable = false)
    private Boolean isExpert = false;

    @Column(columnDefinition = "TEXT")
    private String expertCertificate;

    @Column(columnDefinition = "TEXT")
    private String workProof;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AuditStatus expertAuditStatus = AuditStatus.PENDING;

    @Column(length = 500)
    private String expertAuditRemark;

    private LocalDateTime expertAuditTime;

    @Column(nullable = false)
    private Integer creditScore = 100;

    @Column(nullable = false)
    private Boolean isSuspicious = false;

    @Column(length = 500)
    private String suspiciousReason;

    @Column(nullable = false)
    private Boolean fundsFrozen = false;

    @Column(length = 500)
    private String freezeReason;

    private LocalDateTime freezeTime;

    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(length = 50)
    private String province;

    @Column(length = 50)
    private String city;

    @Column(nullable = false)
    private Boolean enabled = true;

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
