package com.medical.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "挂号请求")
public class RegistrationDTO {
    
    @Schema(description = "号源ID", required = true)
    @NotNull(message = "号源ID不能为空")
    private Long scheduleId;
    
    @Schema(description = "症状描述")
    private String symptoms;
    
    @Schema(description = "备注")
    private String remark;
}
