package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 社团大事记实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_milestone")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团大事记")
public class ClubMilestone extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "事件标题", example = "社团成立")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Schema(description = "事件描述", example = "计算机协会正式成立，首批成员50人")
    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Schema(description = "事件发生日期", example = "2010-09-01")
    @Column(name = "event_date", nullable = false, length = 20)
    private String eventDate;

    @Schema(description = "事件类型：成立/换届/重要活动/获奖/其他", example = "成立")
    @Column(name = "type", length = 50)
    private String type;

    @Schema(description = "相关图片", example = "https://example.com/milestone.jpg")
    @Column(name = "image", length = 500)
    private String image;

    @Schema(description = "排序", example = "1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;
}
