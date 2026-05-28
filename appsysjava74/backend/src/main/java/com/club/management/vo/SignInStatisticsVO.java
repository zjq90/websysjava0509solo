package com.club.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 签到统计视图对象
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "签到统计")
public class SignInStatisticsVO {

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "报名人数")
    private Integer registrationCount;

    @Schema(description = "实到人数")
    private Integer attendedCount;

    @Schema(description = "签到率")
    private Double attendanceRate;

    @Schema(description = "已签到人数")
    private Long signedCount;

    @Schema(description = "未签到人数")
    private Long notSignedCount;

    @Schema(description = "迟到人数")
    private Long lateCount;

    @Schema(description = "补签人数")
    private Long makeUpCount;

    @Schema(description = "缺席人数")
    private Long absentCount;
}
