package com.websys.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 系统配置实体类
 * 
 * @author websys
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_system_config")
public class SystemConfig {

    /**
     * 配置ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配置分组（SYSTEM-系统配置，PAYMENT-支付配置，SMS-短信配置，OTHER-其他）
     */
    @Column(nullable = false, length = 20)
    private String configGroup;

    /**
     * 配置键名
     */
    @Column(nullable = false, unique = true, length = 100)
    private String configKey;

    /**
     * 配置值
     */
    @Column(columnDefinition = "TEXT")
    private String configValue;

    /**
     * 配置名称
     */
    @Column(nullable = false, length = 100)
    private String configName;

    /**
     * 配置描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 配置类型（STRING-字符串，NUMBER-数字，BOOLEAN-布尔值，JSON-JSON对象）
     */
    @Column(nullable = false, length = 20)
    private String configType = "STRING";

    /**
     * 是否启用（1-启用，0-禁用）
     */
    @Column(nullable = false)
    private Integer enabled = 1;

    /**
     * 是否系统内置（1-是，0-否）
     */
    @Column(nullable = false)
    private Integer isSystem = 0;

    /**
     * 排序号
     */
    private Integer sortOrder = 0;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Column(length = 50)
    private String updateBy;

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
