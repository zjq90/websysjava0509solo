package com.teaching.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 50)
    private String realName;

    @Column(length = 10)
    private String gender;

    @Column(length = 20)
    private String phone;

    @Column(length = 500)
    private String avatar;

    @Column(length = 200)
    private String signature;

    @Column(nullable = false)
    private Integer role = 1;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "email_bound")
    private Boolean emailBound = false;

    @Column(name = "reset_code")
    private String resetCode;

    @Column(name = "reset_code_time")
    private LocalDateTime resetCodeTime;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
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
