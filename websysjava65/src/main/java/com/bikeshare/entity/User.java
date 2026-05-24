package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String realName;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal deposit = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer rideCount = 0;

    @Column(nullable = false)
    private Boolean isVip = false;

    @Column(nullable = false)
    private Boolean isBlacklisted = false;

    @Column(length = 20)
    private String userLevel = "NORMAL";

    private LocalDateTime vipExpireTime;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime = LocalDateTime.now();
}
