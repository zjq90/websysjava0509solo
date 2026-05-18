package com.secondhand.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_role")
@Schema(description = "角色实体")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Schema(description = "角色编码: ADMIN-管理员, SELLER-卖家, CUSTOMER_SERVICE-客服, BUYER-买家")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Schema(description = "角色描述")
    @Column(length = 500)
    private String description;

    @Schema(description = "权限列表，JSON格式存储模块权限")
    @Column(columnDefinition = "TEXT")
    private String permissions;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

}