package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户黑名单实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "user_blacklist")
public class UserBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 200)
    private String reason;

    @Column(nullable = false, length = 20)
    private String banType = "PERMANENT";

    private LocalDateTime expireTime;

    @Column(nullable = false, length = 50)
    private String operator;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
}
