package com.club.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 入团申请DTO
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@ApiModel("入团申请参数")
public class ClubApplicationDTO {

    @ApiModelProperty(value = "社团ID", required = true)
    @NotNull(message = "社团ID不能为空")
    private Long clubId;

    @ApiModelProperty("申请理由")
    @NotBlank(message = "申请理由不能为空")
    private String reason;

    @ApiModelProperty("个人简介")
    private String resume;

    @ApiModelProperty("作品链接")
    private String works;
}
