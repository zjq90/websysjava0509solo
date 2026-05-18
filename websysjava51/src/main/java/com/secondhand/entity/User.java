package com.secondhand.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_user")
@Schema(description = "用户实体")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Schema(description = "密码")
    @Column(nullable = false, length = 100)
    private String password;

    @Schema(description = "昵称")
    @Column(length = 50)
    private String nickname;

    @Schema(description = "邮箱")
    @Column(length = 100)
    private String email;

    @Schema(description = "手机号")
    @Column(length = 20)
    private String phone;

    @Schema(description = "头像URL")
    @Column(length = 500)
    private String avatar;

    @Schema(description = "角色ID")
    @Column(nullable = false)
    private Long roleId;

    @Schema(description = "用户状态: ACTIVE-正常, DISABLED-禁用")
    @Column(nullable = false, length = 20)
    private String status;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

}