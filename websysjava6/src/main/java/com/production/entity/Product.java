package com.production.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体类
 * 用于存储生产系统中的产品信息，支持多品种生产
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
     * 产品编号，唯一标识
     */
    @Column(nullable = false, unique = true, length = 50)
    private String productCode;

    /**
     * 产品名称
     */
    @Column(nullable = false, length = 100)
    private String productName;

    /**
     * 产品规格型号
     */
    @Column(length = 100)
    private String specification;

    /**
     * 产品类型，如：种子、化肥等
     */
    @Column(length = 50)
    private String productType;

    /**
     * 产品重量/规格单位
     */
    @Column(length = 20)
    private String unit;

    /**
     * 产品描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 产品状态：ACTIVE(有效), INACTIVE(无效)
     */
    @Column(nullable = false, length = 20)
    private String status;

    // ==================== Getters and Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 在持久化前设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }

    /**
     * 在更新前设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}