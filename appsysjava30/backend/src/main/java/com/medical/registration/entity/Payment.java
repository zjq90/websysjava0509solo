package com.medical.registration.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_payment")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String paymentNo;
    
    @Column(nullable = false, length = 50)
    private String registrationNo;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(length = 20)
    private String paymentMethod;
    
    @Column(length = 50)
    private String tradeNo;
    
    @Column(length = 20)
    private String status;
    
    @Column(length = 200)
    private String remark;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @Column
    private LocalDateTime successTime;
    
    @Column
    private LocalDateTime refundTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
