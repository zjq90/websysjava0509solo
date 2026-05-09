package com.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 品种实体类
 * 管理农产品的品种信息，是成本核算和利润分析的基础维度之一
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "variety")
public class Variety {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 品种编号，唯一标识
     */
    @Column(nullable = false, unique = true, length = 50)
    private String varietyCode;

    /**
     * 品种名称
     */
    @Column(nullable = false, length = 100)
    private String varietyName;

    /**
     * 品种类别（如：水稻、小麦、玉米等）
     */
    @Column(length = 50)
    private String category;

    /**
     * 单位（如：公斤、吨、袋等）
     */
    @Column(nullable = false, length = 20)
    private String unit;

    /**
     * 标准销售价格
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal standardPrice;

    /**
     * 单位标准成本（用于预算和对比）
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal standardCost;

    /**
     * 描述信息
     */
    @Column(length = 500)
    private String description;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    private LocalDateTime updateTime;

    /**
     * 是否启用
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
