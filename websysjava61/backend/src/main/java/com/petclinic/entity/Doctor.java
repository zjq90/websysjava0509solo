package com.petclinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 医生实体类
 */
@Data
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 手机号
     */
    @Column(nullable = false, length = 20)
    private String phone;

    /**
     * 所属科室ID
     */
    @Column(nullable = false)
    private Long departmentId;

    /**
     * 职称
     */
    @Column(length = 50)
    private String title;

    /**
     * 专长
     */
    @Column(length = 500)
    private String specialty;

    /**
     * 执业证编号
     */
    @Column(length = 100)
    private String licenseNumber;

    /**
     * 执业证图片URL
     */
    @Column(length = 500)
    private String licenseImageUrl;

    /**
     * 审核状态：0-待审核，1-审核通过，2-审核驳回
     */
    @Column(nullable = false)
    private Integer auditStatus = 0;

    /**
     * 审核意见
     */
    @Column(length = 500)
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    private Long auditUserId;

    /**
     * 状态：0-离职，1-在职
     */
    @Column(nullable = false)
    private Integer status = 1;

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
