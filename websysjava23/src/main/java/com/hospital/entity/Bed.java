package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 床位实体类
 * 存储床位信息
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "bed")
@Schema(description = "床位")
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "床位ID")
    private Long id;

    @Column(name = "bed_no", unique = true, nullable = false, length = 32)
    @Schema(description = "床位号")
    private String bedNo;

    @Column(name = "ward_name", length = 64)
    @Schema(description = "病区名称")
    private String wardName;

    @Column(name = "room_no", length = 32)
    @Schema(description = "房间号")
    private String roomNo;

    @Column(name = "bed_type", length = 32)
    @Schema(description = "床位类型：普通床/重症监护床/隔离床")
    private String bedType = "普通床";

    @Column(name = "floor", length = 32)
    @Schema(description = "楼层")
    private String floor;

    @Column(name = "department", length = 64)
    @Schema(description = "所属科室")
    private String department;

    @Column(name = "patient_id")
    @Schema(description = "当前患者ID")
    private Long patientId;

    @Transient
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(name = "status", length = 32)
    @Schema(description = "床位状态：空闲/占用/维修中/预留")
    private String status = "空闲";

    @Column(name = "daily_rate", precision = 10, scale = 2)
    @Schema(description = "每日费用")
    private java.math.BigDecimal dailyRate = new java.math.BigDecimal("50");

    @Column(name = "remark", length = 512)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
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
