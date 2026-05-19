package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity {

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "real_name", length = 100)
    private String realName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "preferences", columnDefinition = "TEXT")
    private String preferences;
}
