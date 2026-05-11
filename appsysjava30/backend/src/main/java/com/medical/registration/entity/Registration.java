package com.medical.registration.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "t_registration")
public class Registration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String registrationNo;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long doctorId;
    
    @Column(nullable = false)
    private Long scheduleId;
    
    @Column(nullable = false, length = 50)
    private String deptCode;
    
    @Column(nullable = false)
    private LocalDate visitDate;
    
    @Column(nullable = false)
    private LocalTime visitTime;
    
    @Column(length = 50)
    private String timeSlot;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(length = 20)
    private String paymentMethod;
    
    @Column(length = 50)
    private String paymentStatus;
    
    @Column(length = 50)
    private String refundStatus;
    
    @Column(length = 500)
    private String symptoms;
    
    @Column(length = 20, nullable = false)
    private String status;
    
    @Column(length = 200)
    private String qrCode;
    
    @Column(length = 500)
    private String remark;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @Column
    private LocalDateTime updateTime;
    
    @Column
    private LocalDateTime cancelTime;
    
    @Column
    private LocalDateTime visitCompleteTime;
    
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
