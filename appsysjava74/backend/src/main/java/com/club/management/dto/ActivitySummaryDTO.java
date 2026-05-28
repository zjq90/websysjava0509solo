package com.club.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 活动总结DTO
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "活动总结请求")
public class ActivitySummaryDTO {

    @Schema(description = "总结ID（更新时需要）")
    private Long id;

    @Schema(description = "活动ID", required = true)
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @Schema(description = "总结标题", required = true)
    @NotBlank(message = "总结标题不能为空")
    private String title;

    @Schema(description = "活动总结内容")
    private String content;

    @Schema(description = "活动亮点")
    private String highlights;

    @Schema(description = "活动不足")
    private String shortcomings;

    @Schema(description = "改进建议")
    private String improvements;

    @Schema(description = "成果展示")
    private String achievements;

    @Schema(description = "活动现场照片URL（多个用逗号分隔）")
    private String photoUrls;

    @Schema(description = "附件URL（多个用逗号分隔）")
    private String attachmentUrls;

    @Schema(description = "是否同步到社团主页", example = "true")
    private Boolean syncToClubPage = true;

    @Schema(description = "是否公开可见", example = "true")
    private Boolean isPublic = true;

    @Schema(description = "是否发布", example = "false")
    private Boolean published = false;
}
