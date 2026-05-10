package com.appsys.finance.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "产品名称不能为空")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "批次编号不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{8}$", message = "批次编号必须为8位数字+字母组合")
    @Column(name = "batch_number", unique = true, nullable = false)
    private String batchNumber;

    @NotNull(message = "保质期不能为空")
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @NotNull(message = "发芽率不能为空")
    @DecimalMin(value = "0.0", message = "发芽率不能小于0%")
    @DecimalMax(value = "100.0", message = "发芽率不能大于100%")
    @Column(name = "germination_rate", precision = 4, scale = 1)
    private BigDecimal germinationRate;

    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.0", message = "单价不能小于0")
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "成本价不能为空")
    @DecimalMin(value = "0.0", message = "成本价不能小于0")
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "create_time")
    private LocalDateTime createTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public BigDecimal getGerminationRate() { return germinationRate; }
    public void setGerminationRate(BigDecimal germinationRate) { this.germinationRate = germinationRate; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
