package com.hospital.clinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 药品实体类
 * 用于管理药房的药品信息
 */
@Data
@Entity
@Table(name = "drug")
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drug_code", unique = true, nullable = false, length = 30)
    private String drugCode;

    @Column(name = "drug_name", nullable = false, length = 100)
    private String drugName;

    @Column(name = "generic_name", length = 100)
    private String genericName;

    @Column(name = "drug_spec", length = 100)
    private String drugSpec;

    @Column(name = "drug_type", length = 50)
    private String drugType;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "min_stock")
    private Integer minStock;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "batch_no", length = 50)
    private String batchNo;

    @Column(name = "storage_condition", length = 200)
    private String storageCondition;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
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
