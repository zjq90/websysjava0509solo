package com.psyconsult.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 50)
    private String nickname;

    @Column(length = 500)
    private String avatar;

    @Column(name = "real_name", length = 50)
    private String realName;

    @Column(name = "id_card", length = 18)
    private String idCard;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous = true;

    @Column(name = "encrypt_records", nullable = false)
    private Boolean encryptRecords = true;

    @Column(name = "elder_mode", nullable = false)
    private Boolean elderMode = false;

    @Column(length = 20)
    private String role = "USER";

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(nullable = false)
    private Boolean enabled = true;

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
