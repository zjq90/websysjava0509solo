package com.appsys.system.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统用户实体类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user", indexes = {
    @Index(name = "idx_username", columnList = "username", unique = true),
    @Index(name = "idx_deleted", columnList = "deleted")
})
@Schema(description = "系统用户")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名")
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Schema(description = "密码（加密存储）")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Schema(description = "真实姓名")
    @Column(name = "real_name", length = 50)
    private String realName;

    @Schema(description = "手机号（加密存储）")
    @Column(name = "phone", length = 255)
    private String phone;

    @Schema(description = "邮箱")
    @Column(name = "email", length = 100)
    private String email;

    @Schema(description = "状态：1-启用，0-禁用")
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Schema(description = "头像URL")
    @Column(name = "avatar", length = 255)
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<SysRole> roles = new HashSet<>();
}
