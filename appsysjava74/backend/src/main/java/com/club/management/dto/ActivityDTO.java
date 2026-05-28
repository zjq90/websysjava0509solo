package com.club.management.dto;

import com.club.management.entity.enums.RegistrationScope;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动创建/更新DTO
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "活动创建/更新请求")
public class ActivityDTO {

    @Schema(description = "社团ID", example = "1")
    private Long id;

    @Schema(description = "社团ID", example = "1", required = true)
    @NotNull(message = "社团ID不能为空")
    private Long clubId;

    @Schema(description = "活动名称", example = "2024年迎新晚会", required = true)
    @NotBlank(message = "活动名称不能为空")
    private String name;

    @Schema(description = "活动开始时间", required = true)
    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "活动结束时间", required = true)
    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "活动地点", example = "大礼堂", required = true)
    @NotBlank(message = "活动地点不能为空")
    private String location;

    @Schema(description = "活动名额", example = "100", required = true)
    @NotNull(message = "活动名额不能为空")
    private Integer quota;

    @Schema(description = "报名开始时间", required = true)
    @NotNull(message = "报名开始时间不能为空")
    private LocalDateTime registrationStartTime;

    @Schema(description = "报名结束时间", required = true)
    @NotNull(message = "报名结束时间不能为空")
    private LocalDateTime registrationEndTime;

    @Schema(description = "活动要求")
    private String requirements;

    @Schema(description = "活动简介")
    private String description;

    @Schema(description = "活动海报URL")
    private String posterUrl;

    @Schema(description = "报名范围", example = "ALL_STUDENTS", required = true)
    @NotNull(message = "报名范围不能为空")
    private RegistrationScope registrationScope;

    @Schema(description = "是否需要审核", example = "false")
    private Boolean needApproval = false;

    @Schema(description = "签到开始时间")
    private LocalDateTime signInStartTime;

    @Schema(description = "签到结束时间")
    private LocalDateTime signInEndTime;

    @Schema(description = "活动负责人姓名")
    private String organizerName;

    @Schema(description = "活动负责人电话")
    private String organizerPhone;
}
