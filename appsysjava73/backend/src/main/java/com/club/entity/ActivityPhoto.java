package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 活动照片实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_activity_photo")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "活动照片")
public class ActivityPhoto extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "活动ID", example = "1")
    @Column(name = "activity_id")
    private Long activityId;

    @Schema(description = "照片标题", example = "编程大赛现场")
    @Column(name = "title", length = 200)
    private String title;

    @Schema(description = "照片描述", example = "参赛选手正在紧张作答")
    @Column(name = "description", length = 500)
    private String description;

    @Schema(description = "照片URL", example = "https://example.com/photo1.jpg")
    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Schema(description = "拍摄时间", example = "2024-05-01 15:00:00")
    @Column(name = "shoot_time", length = 50)
    private String shootTime;

    @Schema(description = "拍摄地点", example = "图书馆报告厅")
    @Column(name = "location", length = 200)
    private String location;

    @Schema(description = "排序", example = "1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;
}
