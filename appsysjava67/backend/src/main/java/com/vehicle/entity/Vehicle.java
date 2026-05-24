package com.vehicle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vehicles")
@Schema(description = "车辆信息")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "车辆ID")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "车牌号", example = "京A12345")
    private String plateNumber;

    @Schema(description = "车辆品牌", example = "大众")
    private String brand;

    @Schema(description = "车辆型号", example = "帕萨特")
    private String model;

    @Schema(description = "车身颜色", example = "黑色")
    private String color;

    @Schema(description = "车主姓名", example = "张三")
    private String ownerName;

    @Schema(description = "联系电话", example = "13800138000")
    private String phone;

    @Schema(description = "注册日期")
    private LocalDateTime registerDate;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
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
}
