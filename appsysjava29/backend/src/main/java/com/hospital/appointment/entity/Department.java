package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 科室实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "department")
@Schema(description = "科室信息")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "科室ID")
    private Long id;

    @Column(name = "dept_name", nullable = false, length = 100)
    @Schema(description = "科室名称")
    private String deptName;

    @Column(name = "dept_code", unique = true, length = 50)
    @Schema(description = "科室编码")
    private String deptCode;

    @Column(name = "parent_id")
    @Schema(description = "父科室ID")
    private Long parentId;

    @Column(name = "dept_type", length = 20)
    @Schema(description = "科室类型: 临床/医技/行政")
    private String deptType;

    @Column(name = "location", length = 255)
    @Schema(description = "位置")
    private String location;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "描述")
    private String description;

    @Column(name = "sort_order")
    @Schema(description = "排序")
    private Integer sortOrder;

    @Column(name = "status")
    @Schema(description = "状态: 0-禁用 1-启用")
    private Integer status;

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
        if (sortOrder == null) sortOrder = 0;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
