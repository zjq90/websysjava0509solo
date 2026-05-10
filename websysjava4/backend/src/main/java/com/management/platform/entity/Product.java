package com.management.platform.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 存储商品的基本信息
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * 商品ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 商品分类
     */
    @Column(length = 50)
    private String category;

    /**
     * 商品成本价
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    /**
     * 商品售价
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    /**
     * 库存数量
     */
    @Column(nullable = false)
    private Integer stockQuantity;

    /**
     * 销售数量
     */
    @Column(nullable = false)
    private Integer salesQuantity = 0;

    /**
     * 商品状态：active（在售）, inactive（下架）
     */
    @Column(length = 20)
    private String status = "active";

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

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public Integer getSalesQuantity() { return salesQuantity; }
    public void setSalesQuantity(Integer salesQuantity) { this.salesQuantity = salesQuantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
