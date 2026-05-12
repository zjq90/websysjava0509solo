package com.lims.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 检验检查项目实体类
 * 存储各类检验、检查项目的基础信息
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_item")
@Schema(description = "检验检查项目")
public class TestItem {

    /**
     * 项目ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "项目ID", example = "1")
    private Long id;

    /**
     * 项目编号，唯一标识
     */
    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "项目编号", example = "ITEM001")
    private String itemCode;

    /**
     * 项目名称
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "项目名称", example = "血常规")
    private String itemName;

    /**
     * 项目类型：LABORATORY-检验，EXAMINATION-检查
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "项目类型", example = "LABORATORY")
    private String itemType;

    /**
     * 所属科室ID
     */
    @Column
    @Schema(description = "所属科室ID", example = "1")
    private Long departmentId;

    /**
     * 参考值
     */
    @Column(length = 200)
    @Schema(description = "参考值", example = "4.0-10.0×10^9/L")
    private String referenceValue;

    /**
     * 单位
     */
    @Column(length = 20)
    @Schema(description = "单位", example = "×10^9/L")
    private String unit;

    /**
     * 价格
     */
    @Column(precision = 10, scale = 2)
    @Schema(description = "价格", example = "50.00")
    private BigDecimal price;

    /**
     * 预计完成时间（分钟）
     */
    @Column
    @Schema(description = "预计完成时间(分钟)", example = "60")
    private Integer estimatedTime;

    /**
     * 项目描述
     */
    @Column(length = 500)
    @Schema(description = "项目描述", example = "常规血液检查")
    private String description;

    /**
     * 状态：ACTIVE-启用，INACTIVE-停用
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "状态", example = "ACTIVE")
    private String status;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
