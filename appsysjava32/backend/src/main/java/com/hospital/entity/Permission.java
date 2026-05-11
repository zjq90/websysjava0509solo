package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 权限实体类
 * 系统权限管理
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_permission")
@Schema(description = "权限信息")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "权限ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "权限名称")
    private String permissionName;

    @Column(unique = true, nullable = false, length = 100)
    @Schema(description = "权限编码")
    private String permissionCode;

    @Column(length = 200)
    @Schema(description = "请求路径")
    private String path;

    @Column(length = 10)
    @Schema(description = "请求方法")
    private String method;

    @Column(length = 200)
    @Schema(description = "权限描述")
    private String description;

    @Column
    @Schema(description = "父级权限ID")
    private Long parentId;

    @Column
    @Schema(description = "排序")
    private Integer sortOrder = 0;

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
