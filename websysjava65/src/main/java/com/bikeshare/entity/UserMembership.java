package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户会员实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "user_memberships")
public class UserMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 50)
    private String username;

    @Column(nullable = false, length = 30)
    private String orderNo;

    @Column(nullable = false, length = 20)
    private String planType;

    @Column(nullable = false)
    private Integer days;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 20)
    private String source;

    @Column(length = 200)
    private String remark;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
}
