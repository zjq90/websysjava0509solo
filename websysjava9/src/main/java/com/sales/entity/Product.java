package com.sales.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体类
 * 存储产品基本信息，用于订单中的产品选择
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * 产品ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 产品名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 产品编码，唯一标识
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 产品类别
     */
    @Column(length = 50)
    private String category;

    /**
     * 产品规格
     */
    @Column(length = 100)
    private String specification;

    /**
     * 基础单价（出厂价）
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    /**
     * 库存数量
     */
    @Column(nullable = false)
    private Integer stockQuantity = 0;

    /**
     * 产品单位（如：吨、袋、箱等）
     */
    @Column(length = 20)
    private String unit;

    /**
     * 产品描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 产品状态（启用/禁用）
     */
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
