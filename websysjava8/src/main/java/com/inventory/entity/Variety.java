package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 种子品种实体类
 * 用于管理具体的种子品种信息
 * 每个品种属于一个品类（Category）
 * 例如：蔬菜类下的"番茄一号"、"黄瓜二号"等
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "variety")
public class Variety extends BaseEntity {

    /**
     * 品种名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 品种编码
     */
    @Column(name = "code", unique = true, length = 50)
    private String code;

    /**
     * 所属品类ID
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    /**
     * 所属品类（多对一关系）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    /**
     * 品种描述，包括特征、适宜种植区域等
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 制造商/供应商
     */
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    /**
     * 建议保质期天数（默认365天）
     */
    @Column(name = "shelf_life_days", nullable = false)
    private Integer shelfLifeDays = 365;

    /**
     * 最佳存储温度范围（最小值）
     */
    @Column(name = "optimal_temp_min")
    private Double optimalTempMin;

    /**
     * 最佳存储温度范围（最大值）
     */
    @Column(name = "optimal_temp_max")
    private Double optimalTempMax;

    /**
     * 最佳存储湿度范围（最小值）
     */
    @Column(name = "optimal_humidity_min")
    private Double optimalHumidityMin;

    /**
     * 最佳存储湿度范围（最大值）
     */
    @Column(name = "optimal_humidity_max")
    private Double optimalHumidityMax;

    /**
     * 状态：1-启用，0-禁用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
