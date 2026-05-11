package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医生实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "doctor")
@Schema(description = "医生信息")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "医生ID")
    private Long id;

    @Column(name = "user_id")
    @Schema(description = "关联用户ID")
    private Long userId;

    @Column(name = "dept_id", nullable = false)
    @Schema(description = "科室ID")
    private Long deptId;

    @Column(name = "doctor_name", nullable = false, length = 50)
    @Schema(description = "医生姓名")
    private String doctorName;

    @Column(name = "title", length = 50)
    @Schema(description = "职称: 主任/副主任/主治/住院")
    private String title;

    @Column(name = "title_level", length = 20)
    @Schema(description = "职称级别: senior/deputy/attending/resident")
    private String titleLevel;

    @Column(name = "specialty", length = 255)
    @Schema(description = "专长")
    private String specialty;

    @Column(name = "introduction", columnDefinition = "TEXT")
    @Schema(description = "简介")
    private String introduction;

    @Column(name = "avatar", length = 255)
    @Schema(description = "头像")
    private String avatar;

    @Column(name = "consultation_fee", precision = 10, scale = 2)
    @Schema(description = "挂号费")
    private BigDecimal consultationFee;

    @Column(name = "is_expert")
    @Schema(description = "是否专家: 0-普通 1-专家")
    private Integer isExpert;

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

    @Transient
    @Schema(description = "科室名称")
    private String deptName;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = 1;
        if (isExpert == null) isExpert = 0;
        if (sortOrder == null) sortOrder = 0;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
