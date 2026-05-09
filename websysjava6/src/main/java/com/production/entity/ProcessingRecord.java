package com.production.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 加工流程记录实体类
 * 用于记录清选、包衣、分装等各工序的加工情况
 */
@Entity
@Table(name = "processing_records")
public class ProcessingRecord {

    /**
     * 记录ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 记录编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String recordCode;

    /**
     * 关联的生产计划ID
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", nullable = false)
    private ProductionPlan productionPlan;

    /**
     * 工序类型：CLEANING(清选), COATING(包衣), PACKAGING(分装), INSPECTION(检验)
     */
    @Column(nullable = false, length = 50)
    private String processType;

    /**
     * 工序名称
     */
    @Column(nullable = false, length = 100)
    private String processName;

    /**
     * 工序顺序号
     */
    @Column(nullable = false)
    private Integer processOrder;

    /**
     * 加工数量
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantity;

    /**
     * 合格数量
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal qualifiedQuantity;

    /**
     * 不合格数量
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal unqualifiedQuantity;

    /**
     * 开工时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 完工时间
     */
    private LocalDateTime endTime;

    /**
     * 操作人
     */
    @Column(length = 50)
    private String operator;

    /**
     * 使用设备ID
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    /**
     * 状态：PENDING(待开始), PROCESSING(进行中), COMPLETED(已完成), PAUSED(暂停)
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 加工备注
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

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public ProductionPlan getProductionPlan() {
        return productionPlan;
    }

    public void setProductionPlan(ProductionPlan productionPlan) {
        this.productionPlan = productionPlan;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getProcessOrder() {
        return processOrder;
    }

    public void setProcessOrder(Integer processOrder) {
        this.processOrder = processOrder;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQualifiedQuantity() {
        return qualifiedQuantity;
    }

    public void setQualifiedQuantity(BigDecimal qualifiedQuantity) {
        this.qualifiedQuantity = qualifiedQuantity;
    }

    public BigDecimal getUnqualifiedQuantity() {
        return unqualifiedQuantity;
    }

    public void setUnqualifiedQuantity(BigDecimal unqualifiedQuantity) {
        this.unqualifiedQuantity = unqualifiedQuantity;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}