package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 员工实体类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "employee")
@Schema(description = "员工信息")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "员工ID", example = "1")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @Schema(description = "员工姓名", example = "李摄影")
    private String name;

    @Column(name = "phone", length = 20)
    @Schema(description = "手机号码", example = "13900139000")
    private String phone;

    @Column(name = "email", length = 100)
    @Schema(description = "邮箱", example = "li@photostudio.com")
    private String email;

    @Column(name = "position", length = 50)
    @Schema(description = "职位: 摄影师/化妆师/修图师/设计师/销售/其他", example = "摄影师")
    private String position;

    @Column(name = "specialty", length = 200)
    @Schema(description = "专长: 人像/风景/调色/婚纱/儿童/全家福等，逗号分隔", example = "人像,婚纱")
    private String specialty;

    @Column(name = "level")
    @Schema(description = "等级: 1-初级 2-中级 3-高级 4-资深", example = "3")
    private Integer level;

    @Column(name = "avatar", length = 255)
    @Schema(description = "头像")
    private String avatar;

    @Column(name = "status")
    @Schema(description = "状态: 0-离职 1-在职", example = "1")
    private Integer status;

    @Column(name = "is_external")
    @Schema(description = "是否外包: 0-内部 1-外包", example = "0")
    private Integer isExternal;

    @Column(name = "task_count")
    @Schema(description = "当前任务数量", example = "5")
    private Integer taskCount;

    @Column(name = "completed_count")
    @Schema(description = "累计完成任务数量", example = "128")
    private Integer completedCount;

    @Column(name = "rating")
    @Schema(description = "评分: 1-5分", example = "4.8")
    private java.math.BigDecimal rating;

    @Column(name = "sort_order")
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = 1;
        }
        if (isExternal == null) {
            isExternal = 0;
        }
        if (taskCount == null) {
            taskCount = 0;
        }
        if (completedCount == null) {
            completedCount = 0;
        }
        if (rating == null) {
            rating = new java.math.BigDecimal("5.0");
        }
        if (sortOrder == null) {
            sortOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
