package com.seedtrace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 亲本信息实体类
 * 
 * <p>存储种子的亲本来源信息，包括母本和父本的详细信息。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parent_info")
public class ParentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次ID（外键）
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 母本编号
     */
    @Column(length = 50)
    private String femaleParentCode;

    /**
     * 母本名称
     */
    @Column(length = 100)
    private String femaleParentName;

    /**
     * 母本来源地
     */
    @Column(length = 200)
    private String femaleParentOrigin;

    /**
     * 父本编号
     */
    @Column(length = 50)
    private String maleParentCode;

    /**
     * 父本名称
     */
    @Column(length = 100)
    private String maleParentName;

    /**
     * 父本来源地
     */
    @Column(length = 200)
    private String maleParentOrigin;

    /**
     * 育种方式
     */
    @Column(length = 100)
    private String breedingMethod;

    /**
     * 育种单位
     */
    @Column(length = 100)
    private String breedingOrganization;

    /**
     * 育种年份
     */
    private Integer breedingYear;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
