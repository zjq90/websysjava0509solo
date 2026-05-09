package com.appsys.production.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "production_batch")
public class ProductionBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String batchNo;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private Double quantity;

    @Column(length = 50)
    private String unit;

    @Column(nullable = false)
    private LocalDate shelfLife;

    @Column(length = 100)
    private String customerName;

    @Column(length = 255)
    private String customerPhoneEncrypted;

    @Column(length = 20)
    private String status;

    @Column(length = 50)
    private String currentStage;

    @Column(columnDefinition = "TEXT")
    private String remark;

    private Long operatorId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
