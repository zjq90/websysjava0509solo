package com.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Schema(description = "执法记录DTO")
public class EnforcementRecordDTO {

    @NotBlank(message = "车牌号不能为空")
    @Schema(description = "车牌号", required = true)
    private String plateNumber;

    @Schema(description = "违规类型")
    private String violationType;

    @Schema(description = "违规描述")
    private String violationDescription;

    @Schema(description = "处罚金额")
    private BigDecimal penaltyAmount;

    @Schema(description = "执法地点")
    private String location;

    @Schema(description = "经度")
    private Double longitude;

    @Schema(description = "纬度")
    private Double latitude;

    @Schema(description = "现场照片URL（多个用逗号分隔）")
    private String photoUrls;

    @Schema(description = "执法人姓名")
    private String officerName;

    @Schema(description = "执法人签名数据（Base64）")
    private String officerSignature;

    @Schema(description = "当事人签名数据（Base64）")
    private String partySignature;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "是否离线创建")
    private Boolean offlineCreated;
}
