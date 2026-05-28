package com.club.management.dto;

import com.club.management.entity.enums.RegistrationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 审核请求DTO
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "审核请求")
public class AuditDTO {

    @Schema(description = "报名记录ID", example = "1", required = true)
    @NotNull(message = "报名记录ID不能为空")
    private Long registrationId;

    @Schema(description = "审核状态", example = "APPROVED", required = true)
    @NotNull(message = "审核状态不能为空")
    private RegistrationStatus status;

    @Schema(description = "审核意见")
    private String remark;
}
