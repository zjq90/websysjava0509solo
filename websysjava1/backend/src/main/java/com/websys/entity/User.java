package com.websys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类
 * 系统用户信息，包含基本信息、角色关联等
 */
@Entity
@Table(name = "sys_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "用户名")
    private String username;

    @Column(nullable = false, length = 100)
    @Schema(description = "密码")
    private String password;

    @Column(length = 50)
    @Schema(description = "真实姓名")
    private String realName;

    @Column(length = 20)
    @Schema(description = "手机号")
    private String phone;

    @Column(length = 100)
    @Schema(description = "邮箱")
    private String email;

    @Column(nullable = false)
    @Schema(description = "状态：1启用，0禁用")
    private Integer status = 1;

    @Column(name = "last_login_time")
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "用户角色列表")
    private List<Role> roles;
}
