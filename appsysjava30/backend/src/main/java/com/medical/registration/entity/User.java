package com.medical.registration.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false, length = 100)
    private String password;
    
    @Column(nullable = false, length = 100)
    private String realName;
    
    @Column(length = 50)
    private String idCard;
    
    @Column(length = 50)
    private String phone;
    
    @Column(length = 200)
    private String address;
    
    @Column(length = 10)
    private String gender;
    
    @Column
    private Integer age;
    
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
