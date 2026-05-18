package com.heritage.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String nickname;

    private String avatar;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "user_type")
    private Integer userType = 1;

    @Column(name = "is_verified")
    private Integer isVerified = 0;

    @Column(name = "is_elder_mode")
    private Integer isElderMode = 0;

    private Integer status = 1;

    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "frozen_balance")
    private BigDecimal frozenBalance = BigDecimal.ZERO;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}
