package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_user")
@Schema(description = "用户信息")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID")
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    @Schema(description = "用户名")
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    @Schema(description = "密码")
    private String password;

    @Column(name = "real_name", length = 50)
    @Schema(description = "真实姓名")
    private String realName;

    @Column(name = "phone", length = 20)
    @Schema(description = "手机号")
    private String phone;

    @Column(name = "id_card", length = 50)
    @Schema(description = "身份证号")
    private String idCard;

    @Column(name = "email", length = 100)
    @Schema(description = "邮箱")
    private String email;

    @Column(name = "avatar", length = 255)
    @Schema(description = "头像")
    private String avatar;

    @Column(name = "gender")
    @Schema(description = "性别: 0-未知 1-男 2-女")
    private Integer gender;

    @Column(name = "birthday")
    @Schema(description = "生日")
    private LocalDate birthday;

    @Column(name = "address", length = 255)
    @Schema(description = "地址")
    private String address;

    @Column(name = "status")
    @Schema(description = "状态: 0-禁用 1-启用")
    private Integer status;

    @Column(name = "elder_mode")
    @Schema(description = "长辈模式: 0-普通 1-长辈模式")
    private Integer elderMode;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = 1;
        if (elderMode == null) elderMode = 0;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
