package com.agriculture.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 观测模板实体类
 * 支持模板预设（如玉米、小麦标准观测项），减少重复输入
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "observation_templates")
public class ObservationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板名称
     */
    @Column(nullable = false, length = 100)
    private String templateName;

    /**
     * 适用作物类型：CORN-玉米，WHEAT-小麦，RICE-水稻等
     */
    @Column(nullable = false, length = 20)
    private String cropType;

    /**
     * 适用生长阶段，多个用逗号分隔
     */
    @Column(length = 200)
    private String applicableStages;

    /**
     * 观测项配置（JSON格式）
     * 格式示例：
     * [
     *   {"field":"plantHeight","label":"株高(cm)","type":"number","required":true},
     *   {"field":"emergenceRate","label":"出苗率(%)","type":"number","required":false},
     *   {"field":"pestLevel","label":"病虫害等级","type":"select","options":["0-无","1-轻度","2-中度"],"required":true}
     * ]
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String fieldsConfig;

    /**
     * 是否为系统预设模板
     */
    @Column(nullable = false)
    private Boolean isSystem;

    /**
     * 创建人ID（自定义模板）
     */
    private Long createdBy;

    /**
     * 模板描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 状态：ACTIVE-启用，INACTIVE-停用
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
        if (isSystem == null) {
            isSystem = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
