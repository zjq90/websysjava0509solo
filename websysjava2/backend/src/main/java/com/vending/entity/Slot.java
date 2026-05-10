package com.vending.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 货道实体类
 * 绑定商品与售货机货道
 */
@Data
@Entity
@Table(name = "slot", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"machine_id", "slot_number"})
})
@Schema(description = "售货机货道")
public class Slot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "货道ID")
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id", nullable = false)
    @JsonIgnore
    @Schema(description = "所属售货机", required = true)
    private VendingMachine machine;
    
    @Column(name = "slot_number", nullable = false)
    @Schema(description = "货道编号", required = true)
    private Integer slotNumber;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @Schema(description = "绑定的商品")
    private Product product;
    
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Schema(description = "当前库存")
    private Integer currentStock = 0;
    
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Schema(description = "最大容量")
    private Integer maxCapacity = 10;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Schema(description = "是否启用")
    private Boolean enabled = true;
    
    @Schema(description = "最近补货时间")
    private LocalDateTime lastRefillTime;
    
    @PreUpdate
    protected void onUpdate() {
    }
}
