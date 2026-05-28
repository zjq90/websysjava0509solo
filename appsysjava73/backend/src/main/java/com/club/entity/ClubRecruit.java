package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 社团招新实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_recruit")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团招新信息")
public class ClubRecruit extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "社团名称", example = "计算机协会")
    @Column(name = "club_name", length = 100)
    private String clubName;

    @Schema(description = "社团Logo", example = "https://example.com/club-logo.png")
    @Column(name = "club_logo", length = 500)
    private String clubLogo;

    @Schema(description = "招新标题", example = "2024年春季招新")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Schema(description = "招新介绍", example = "欢迎热爱编程的同学加入我们！")
    @Column(name = "introduction", nullable = false, length = 2000)
    private String introduction;

    @Schema(description = "招新要求", example = "1. 热爱编程 2. 有Java基础优先")
    @Column(name = "requirements", length = 2000)
    private String requirements;

    @Schema(description = "招新名额", example = "50")
    @Column(name = "quota", nullable = false)
    private Integer quota;

    @Schema(description = "已申请人数", example = "30")
    @Column(name = "apply_count", nullable = false)
    private Integer applyCount = 0;

    @Schema(description = "已通过人数", example = "20")
    @Column(name = "approved_count", nullable = false)
    private Integer approvedCount = 0;

    @Schema(description = "招新开始时间", example = "2024-03-01 00:00:00")
    @Column(name = "start_time", nullable = false, length = 50)
    private String startTime;

    @Schema(description = "招新截止时间", example = "2024-03-31 23:59:59")
    @Column(name = "end_time", nullable = false, length = 50)
    private String endTime;

    @Schema(description = "联系人", example = "张三")
    @Column(name = "contact_name", length = 50)
    private String contactName;

    @Schema(description = "联系电话", example = "13800138000")
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Schema(description = "状态：0-关闭 1-开启", example = "1")
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
