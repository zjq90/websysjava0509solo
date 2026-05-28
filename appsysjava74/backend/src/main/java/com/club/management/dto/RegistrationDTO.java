package com.club.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 报名请求DTO
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "报名请求")
public class RegistrationDTO {

    @Schema(description = "活动ID", example = "1", required = true)
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @Schema(description = "报名备注")
    private String remark;
}
