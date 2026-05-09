package com.inventory.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 调拨实体类
 * 功能：管理库存调拨操作，支持仓库间、门店间调拨
 */
@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "调拨单号不能为空")
    @Size(min = 1, max = 50, message = "调拨单号长度必须在1-50之间")
    private String transferCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seed_batch_id", nullable = false)
    @NotNull(message = "种子批次不能为空")
    private SeedBatch seedBatch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_warehouse_id")
    private Warehouse fromWarehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_store_id")
    private Store fromStore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_warehouse_id")
    private Warehouse toWarehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_store_id")
    private Store toStore;

    @Column(nullable = false)
    @NotNull(message = "调拨数量不能为空")
    @Min(value = 1, message = "调拨数量必须大于0")
    private Integer quantity;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "状态不能为空")
    private String status;

    @Column(length = 500)
    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTransferCode() { return transferCode; }
    public void setTransferCode(String transferCode) { this.transferCode = transferCode; }
    public SeedBatch getSeedBatch() { return seedBatch; }
    public void setSeedBatch(SeedBatch seedBatch) { this.seedBatch = seedBatch; }
    public Warehouse getFromWarehouse() { return fromWarehouse; }
    public void setFromWarehouse(Warehouse fromWarehouse) { this.fromWarehouse = fromWarehouse; }
    public Store getFromStore() { return fromStore; }
    public void setFromStore(Store fromStore) { this.fromStore = fromStore; }
    public Warehouse getToWarehouse() { return toWarehouse; }
    public void setToWarehouse(Warehouse toWarehouse) { this.toWarehouse = toWarehouse; }
    public Store getToStore() { return toStore; }
    public void setToStore(Store toStore) { this.toStore = toStore; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
