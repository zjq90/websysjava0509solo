package com.production.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 生产计划实体类
 * 用于可视化排产功能，支持多品种、多批次并行生产调度
 */
@Entity
@Table(name = "production_plans")
public class ProductionPlan {

    /**
     * 计划ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 计划编号，唯一标识
     */
    @Column(nullable = false, unique = true, length = 50)
    private String planCode;

    /**
     * 计划名称
     */
    @Column(nullable = false, length = 100)
    private String planName;

    /**
     * 关联的产品ID
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * 批次号
     */
    @Column(nullable = false, length = 50)
    private String batchNumber;

    /**
     * 计划生产数量
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal planQuantity;

    /**
     * 已生产数量
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal producedQuantity = BigDecimal.ZERO;

    /**
     * 计划开始日期
     */
    @Column(nullable = false)
    private LocalDate planStartDate;

    /**
     * 计划完成日期
     */
    @Column(nullable = false)
    private LocalDate planEndDate;

    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    private LocalDateTime actualEndTime;

    /**
     * 生产车间/生产线
     */
    @Column(length = 50)
    private String productionLine;

    /**
     * 优先级：HIGH(高), MEDIUM(中), LOW(低)
     */
    @Column(length = 20)
    private String priority = "MEDIUM";

    /**
     * 计划状态：DRAFT(草稿), PENDING(待执行), RUNNING(执行中), PAUSED(暂停), COMPLETED(已完成), CANCELLED(已取消)
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 负责人
     */
    @Column(length = 50)
    private String responsiblePerson;

    /**
     * 备注说明
     */
    @Column(columnDefinition = "TEXT")
    private String remarks;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // ==================== Getters and Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public BigDecimal getPlanQuantity() {
        return planQuantity;
    }

    public void setPlanQuantity(BigDecimal planQuantity) {
        this.planQuantity = planQuantity;
    }

    public BigDecimal getProducedQuantity() {
        return producedQuantity;
    }

    public void setProducedQuantity(BigDecimal producedQuantity) {
        this.producedQuantity = producedQuantity;
    }

    public LocalDate getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(LocalDate planStartDate) {
        this.planStartDate = planStartDate;
    }

    public LocalDate getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(LocalDate planEndDate) {
        this.planEndDate = planEndDate;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(String productionLine) {
        this.productionLine = productionLine;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        if (this.status == null) {
            this.status = "PENDING";
        }
        if (this.producedQuantity == null) {
            this.producedQuantity = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 计算进度百分比（用于前端显示进度条）
     */
    public int getProgressPercent() {
        if (this.planQuantity == null || this.planQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        if (this.producedQuantity == null) {
            return 0;
        }
        return this.producedQuantity.multiply(new BigDecimal("100"))
                .divide(this.planQuantity, 0, BigDecimal.ROUND_DOWN)
                .intValue();
    }

    /**
     * 显示进度文本
     */
    public String getProgressText() {
        if (this.producedQuantity == null) {
            return "0/" + (this.planQuantity != null ? this.planQuantity : "0");
        }
        return this.producedQuantity + "/" + this.planQuantity;
    }
}