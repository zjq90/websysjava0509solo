package com.appsys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "id_card", length = 20)
    private String idCard;

    @Column(name = "real_name", length = 50)
    private String realName;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "elder_mode", nullable = false)
    private Boolean elderMode = false;
}
