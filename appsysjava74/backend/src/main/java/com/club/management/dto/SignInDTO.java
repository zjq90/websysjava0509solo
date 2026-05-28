package com.club.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 签到请求DTO
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "签到请求")
public class SignInDTO {

    @Schema(description = "活动ID", example = "1", required = true)
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @Schema(description = "二维码Token", required = true)
    @NotBlank(message = "二维码Token不能为空")
    private String qrToken;

    @Schema(description = "签到位置（经纬度）")
    private String location;

    @Schema(description = "设备信息")
    private String deviceInfo;
}
