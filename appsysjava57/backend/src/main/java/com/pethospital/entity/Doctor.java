package com.pethospital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医生实体类
 * 存储医生的基本信息和执业信息
 * 
 * @author Pet Hospital Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 200)
    private String avatar;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(length = 100)
    private String title;

    @Column(length = 500)
    private String specialties;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    private Integer experienceYears;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 500)
    private String licenseNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal consultationFee;

    private Double rating = 5.0;

    private Integer reviewCount = 0;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(updatable = false)
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
