package com.medical.registration.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_operation_log")
public class OperationLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String operationType;
    
    @Column(nullable = false, length = 200)
    private String operationDesc;
    
    @Column(length = 50)
    private String module;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(length = 50)
    private String username;
    
    @Column(length = 200)
    private String requestUrl;
    
    @Column(length = 500)
    private String requestParams;
    
    @Column(length = 50)
    private String ipAddress;
    
    @Column(length = 50)
    private String status;
    
    @Column(length = 500)
    private String errorMsg;
    
    @Column(nullable = false)
    private LocalDateTime operationTime;
    
    @Column(nullable = false)
    private Long duration;
    
    @PrePersist
    protected void onCreate() {
        operationTime = LocalDateTime.now();
    }
}
