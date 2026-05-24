package com.vehicle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tasks")
@Schema(description = "查验任务")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "任务ID")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "任务编号", example = "TASK20240101001")
    private String taskNo;

    @Column(nullable = false)
    @Schema(description = "车牌号", example = "京C66666")
    private String plateNumber;

    @Schema(description = "任务类型", example = "SUSPICIOUS_VEHICLE")
    private String taskType;

    @Schema(description = "任务描述", example = "该车辆涉嫌套牌，请现场查验")
    private String description;

    @Schema(description = "优先级", example = "HIGH")
    private String priority;

    @Schema(description = "最后出现地点", example = "北京市海淀区中关村大街")
    private String lastLocation;

    @Schema(description = "最后出现经度", example = "116.3198")
    private Double lastLongitude;

    @Schema(description = "最后出现纬度", example = "39.9891")
    private Double lastLatitude;

    @Schema(description = "最后出现时间")
    private LocalDateTime lastSeenTime;

    @Schema(description = "指派执法人员", example = "赵警官")
    private String assignedOfficer;

    @Schema(description = "任务状态: PENDING-待处理, IN_PROGRESS-处理中, COMPLETED-已完成, CANCELLED-已取消", example = "PENDING")
    private String status;

    @Schema(description = "任务创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "任务更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "任务完成时间")
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
