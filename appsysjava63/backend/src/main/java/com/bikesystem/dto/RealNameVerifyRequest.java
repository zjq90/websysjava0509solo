package com.bikesystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 实名认证请求DTO
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@Schema(description = "实名认证请求")
public class RealNameVerifyRequest {

    @NotBlank(message = "真实姓名不能为空")
    @Schema(description = "真实姓名", required = true)
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", message = "身份证号格式不正确")
    @Schema(description = "身份证号", required = true)
    private String idCard;

    @Schema(description = "人脸图片Base64")
    private String faceImage;
}
