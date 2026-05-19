package com.pethospital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 存储系统用户的基本信息
 * 
 * @author Pet Hospital Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String realName;

    @Column(length = 200)
    private String avatar;

    @Column(length = 10)
    private String gender;

    private Integer age;

    @Column(length = 200)
    private String address;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(length = 20)
    private String role = "USER";

    private Boolean elderMode = false;

    @Column(updatable = false)
    private LocalDateTime createTime;

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
