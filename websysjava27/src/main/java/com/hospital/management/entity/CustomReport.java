package com.hospital.management.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 自定义报表实体类
 * 存储用户自定义的报表配置信息
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "custom_report")
public class CustomReport {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 报表名称
     */
    @Column(name = "report_name", nullable = false, length = 200)
    private String reportName;

    /**
     * 报表类型: OPERATION-运营指标, QUALITY-医疗质量, COST-成本效益
     */
    @Column(name = "report_type", length = 50)
    private String reportType;

    /**
     * 报表描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 统计维度配置(JSON格式)
     */
    @Column(name = "dimensions", columnDefinition = "TEXT")
    private String dimensions;

    /**
     * 统计指标配置(JSON格式)
     */
    @Column(name = "metrics", columnDefinition = "TEXT")
    private String metrics;

    /**
     * 过滤条件配置(JSON格式)
     */
    @Column(name = "filters", columnDefinition = "TEXT")
    private String filters;

    /**
     * 图表类型: LINE-折线图, BAR-柱状图, PIE-饼图, TABLE-表格
     */
    @Column(name = "chart_type", length = 50)
    private String chartType;

    /**
     * 报表格式配置(JSON格式)
     */
    @Column(name = "format_config", columnDefinition = "TEXT")
    private String formatConfig;

    /**
     * 创建人
     */
    @Column(name = "creator", length = 100)
    private String creator;

    /**
     * 是否启用
     */
    @Column(name = "enabled")
    private Boolean enabled = true;

    /**
     * 是否为系统预设报表
     */
    @Column(name = "is_system")
    private Boolean isSystem = false;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
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
