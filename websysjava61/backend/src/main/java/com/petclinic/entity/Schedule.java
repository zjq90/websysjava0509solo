package com.petclinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 排班实体类
 */
@Data
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * 排班日期
     */
    @Column(nullable = false)
    private LocalDate scheduleDate;

    /**
     * 开始时间
     */
    @Column(nullable = false)
    private LocalTime startTime;

    /**
     * 结束时间
     */
    @Column(nullable = false)
    private LocalTime endTime;

    /**
     * 最大接诊数
     */
    @Column(nullable = false)
    private Integer maxCount = 10;

    /**
     * 已预约数
     */
    @Column(nullable = false)
    private Integer bookedCount = 0;

    /**
     * 状态：0-禁用，1-启用
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

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
