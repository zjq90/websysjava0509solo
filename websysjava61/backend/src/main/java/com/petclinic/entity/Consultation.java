package com.petclinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 问诊记录实体类
 */
@Data
@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 问诊单号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String consultationNo;

    /**
     * 宠物主人ID
     */
    @Column(nullable = false)
    private Long petOwnerId;

    /**
     * 医生ID
     */
    @Column(nullable = false)
    private Long doctorId;

    /**
     * 科室ID
     */
    @Column(nullable = false)
    private Long departmentId;

    /**
     * 宠物名称
     */
    @Column(length = 50)
    private String petName;

    /**
     * 宠物类型（猫、狗等）
     */
    @Column(length = 20)
    private String petType;

    /**
     * 宠物年龄
     */
    private Integer petAge;

    /**
     * 病情描述
     */
    @Column(length = 2000)
    private String symptom;

    /**
     * 诊断结果
     */
    @Column(length = 2000)
    private String diagnosis;

    /**
     * 医嘱
     */
    @Column(length = 2000)
    private String advice;

    /**
     * 是否急诊：0-否，1-是
     */
    @Column(nullable = false)
    private Integer isEmergency = 0;

    /**
     * 问诊费用
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal fee;

    /**
     * 状态：0-待接诊，1-问诊中，2-已完成，3-已取消
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 预约时间
     */
    private LocalDateTime appointmentTime;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
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
