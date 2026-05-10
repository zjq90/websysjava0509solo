package com.vending.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 补货单实体类
 * 智能补货功能：当库存低于阈值时自动生成
 */
@Data
@Entity
@Table(name = "restock_order")
@Schema(description = "补货单")
public class RestockOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "补货单ID")
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "补货单号", required = true)
    private String restockNo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id", nullable = false)
    @Schema(description = "目标售货机", required = true)
    private VendingMachine machine;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    @Schema(description = "状态：PENDING待处理, IN_PROGRESS补货中, COMPLETED已完成, CANCELLED已取消")
    private String status = "PENDING";
    
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'AUTO'")
    @Schema(description = "创建方式：AUTO自动生成, MANUAL手动创建")
    private String createType = "AUTO";
    
    @Schema(description = "补货员")
    private String operator;
    
    @Schema(description = "备注")
    @Column(length = 500)
    private String remark;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "是否已提醒")
    private Boolean notified = false;
    
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "完成时间")
    private LocalDateTime completeTime;
    
    @OneToMany(mappedBy = "restockOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Schema(description = "补货明细")
    private List<RestockOrderItem> items = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
