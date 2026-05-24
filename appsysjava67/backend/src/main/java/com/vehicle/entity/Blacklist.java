package com.vehicle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blacklist")
@Schema(description = "黑名单车辆")
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "黑名单ID")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "车牌号", example = "京B88888")
    private String plateNumber;

    @Schema(description = "违规原因", example = "套牌车辆")
    private String reason;

    @Schema(description = "违规描述", example = "该车辆涉嫌套用其他车辆号牌")
    private String description;

    @Schema(description = "风险等级", example = "HIGH")
    private String riskLevel;

    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;

    @Schema(description = "录入人", example = "李警官")
    private String creator;

    @Schema(description = "录入时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (enabled == null) {
            enabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
