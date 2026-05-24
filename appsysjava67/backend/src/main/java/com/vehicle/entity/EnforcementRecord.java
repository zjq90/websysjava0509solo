package com.vehicle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "enforcement_records")
@Schema(description = "执法记录")
public class EnforcementRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "车牌号", example = "京A12345")
    private String plateNumber;

    @Schema(description = "违规类型", example = "TRAFFIC_VIOLATION")
    private String violationType;

    @Schema(description = "违规描述", example = "闯红灯")
    private String violationDescription;

    @Schema(description = "处罚金额", example = "200.00")
    private BigDecimal penaltyAmount;

    @Schema(description = "执法地点", example = "北京市朝阳区建国路")
    private String location;

    @Schema(description = "经度", example = "116.4074")
    private Double longitude;

    @Schema(description = "纬度", example = "39.9042")
    private Double latitude;

    @Schema(description = "现场照片URL（多个用逗号分隔）", example = "/photos/photo1.jpg,/photos/photo2.jpg")
    @Column(length = 2000)
    private String photoUrls;

    @Schema(description = "执法人姓名", example = "王警官")
    private String officerName;

    @Schema(description = "执法人签名数据（Base64）")
    @Column(length = 5000)
    private String officerSignature;

    @Schema(description = "当事人签名数据（Base64）")
    @Column(length = 5000)
    private String partySignature;

    @Schema(description = "备注")
    @Column(length = 1000)
    private String remarks;

    @Schema(description = "记录状态", example = "PENDING")
    private String status;

    @Schema(description = "是否已同步", example = "true")
    private Boolean synced;

    @Schema(description = "执法时间")
    private LocalDateTime enforcementTime;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        enforcementTime = LocalDateTime.now();
        if (synced == null) {
            synced = true;
        }
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
