package com.club.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 活动评分DTO
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "活动评分请求")
public class ActivityRatingDTO {

    @Schema(description = "活动ID", required = true)
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @Schema(description = "总体评分（1-5星）", example = "5", required = true)
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer rating;

    @Schema(description = "活动内容评分（1-5星）", example = "5")
    @Min(value = 1, message = "内容评分最小为1")
    @Max(value = 5, message = "内容评分最大为5")
    private Integer contentRating;

    @Schema(description = "组织安排评分（1-5星）", example = "5")
    @Min(value = 1, message = "组织评分最小为1")
    @Max(value = 5, message = "组织评分最大为5")
    private Integer organizationRating;

    @Schema(description = "场地环境评分（1-5星）", example = "5")
    @Min(value = 1, message = "场地评分最小为1")
    @Max(value = 5, message = "场地评分最大为5")
    private Integer venueRating;

    @Schema(description = "评价内容")
    private String comment;

    @Schema(description = "反馈建议")
    private String suggestion;

    @Schema(description = "是否匿名", example = "false")
    private Boolean anonymous = false;
}
