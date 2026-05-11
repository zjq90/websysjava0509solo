package com.medical.registration.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_doctor")
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String deptCode;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 20)
    private String title;
    
    @Column(length = 1000)
    private String introduction;
    
    @Column(length = 500)
    private String specialties;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal registrationFee;
    
    @Column
    private Integer sortOrder = 0;
    
    @Column(nullable = false)
    private Integer status = 1;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @Column
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
