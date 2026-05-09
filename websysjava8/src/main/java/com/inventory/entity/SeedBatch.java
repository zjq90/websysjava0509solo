package com.inventory.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 种子批次实体类
 * 功能：管理种子的批次信息，包含保质期等关键属性
 */
@Entity
@Table(name = "seed_batch")
public class SeedBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variety_id", nullable = false)
    @NotNull(message = "品种不能为空")
    private Variety variety;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "批次编码不能为空")
    @Size(min = 1, max = 50, message = "批次编码长度必须在1-50之间")
    private String batchCode;

    @Column(nullable = false)
    @NotNull(message = "生产日期不能为空")
    private LocalDate productionDate;

    @Column(nullable = false)
    @NotNull(message = "过期日期不能为空")
    private LocalDate expiryDate;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "供应商不能为空")
    @Size(min = 1, max = 100, message = "供应商名称长度必须在1-100之间")
    private String supplier;

    @Column(precision = 10, scale = 2)
    @Positive(message = "进货价格必须为正数")
    private BigDecimal purchasePrice;

    @Column(length = 500)
    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;

    private LocalDateTime createTime;

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
    public Variety getVariety() { return variety; }
    public void setVariety(Variety variety) { this.variety = variety; }
    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }
    public LocalDate getProductionDate() { return productionDate; }
    public void setProductionDate(LocalDate productionDate) { this.productionDate = productionDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public java.math.BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(java.math.BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
