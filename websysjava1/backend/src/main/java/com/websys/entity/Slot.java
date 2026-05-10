package com.websys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 货道实体类
 * 管理设备货道中的商品信息，包括商品种类、总量、余量、货道状况等
 */
@Entity
@Table(name = "biz_slot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "货道")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "货道ID")
    private Long id;

    @Column(name = "device_id", nullable = false)
    @Schema(description = "设备ID")
    private Long deviceId;

    @Column(name = "device_code", nullable = false, length = 50)
    @Schema(description = "设备编号")
    private String deviceCode;

    @Column(name = "device_name", length = 100)
    @Schema(description = "设备名称")
    private String deviceName;

    @Column(name = "slot_no", nullable = false, length = 10)
    @Schema(description = "货道编号")
    private String slotNo;

    @Column(name = "slot_code", length = 20)
    @Schema(description = "货道编码")
    private String slotCode;

    @Column(name = "slot_index")
    @Schema(description = "货道号")
    private Integer slotIndex;

    @Column(name = "product_id")
    @Schema(description = "商品ID")
    private Long productId;

    @Column(name = "product_code", length = 50)
    @Schema(description = "商品编码")
    private String productCode;

    @Column(name = "product_name", length = 100)
    @Schema(description = "商品名称")
    private String productName;

    @Column(name = "product_price", precision = 10, scale = 2)
    @Schema(description = "商品价格")
    private BigDecimal productPrice;

    @Column(name = "product_unit", length = 10)
    @Schema(description = "商品单位")
    private String productUnit;

    @Column(nullable = false)
    @Schema(description = "货道容量（总量）")
    private Integer capacity = 20;

    @Column(name = "max_capacity")
    @Schema(description = "最大容量")
    private Integer maxCapacity = 20;

    @Column(nullable = false)
    @Schema(description = "当前库存（余量）")
    private Integer stock = 0;

    @Column(name = "current_quantity")
    @Schema(description = "当前数量")
    private Integer currentQuantity = 0;

    @Column(nullable = false)
    @Schema(description = "货道状态：1正常，2故障，3缺货")
    private Integer status = 1;

    @Column(length = 200)
    @Schema(description = "货道备注")
    private String remark;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Column(name = "last_update_time")
    @Schema(description = "最后更新时间")
    private LocalDateTime lastUpdateTime;
}
