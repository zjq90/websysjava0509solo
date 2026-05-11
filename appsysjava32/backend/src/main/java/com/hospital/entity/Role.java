package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色实体类
 * 系统角色管理
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_role")
@Schema(description = "角色信息")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "角色ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "角色名称")
    private String roleName;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "角色编码")
    private String roleCode;

    @Column(length = 200)
    @Schema(description = "角色描述")
    private String description;

    @Column(nullable = false)
    @Schema(description = "状态：0禁用 1启用")
    private Integer status = 1;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_role_permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Schema(description = "角色权限列表")
    private Set<Permission> permissions = new HashSet<>();

    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
