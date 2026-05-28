package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 社团荣誉实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_honor")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团荣誉")
public class ClubHonor extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "荣誉名称", example = "全国大学生编程大赛一等奖")
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Schema(description = "荣誉级别：国家级/省级/校级/院级", example = "国家级")
    @Column(name = "level", length = 50)
    private String level;

    @Schema(description = "颁发机构", example = "教育部")
    @Column(name = "issuer", length = 100)
    private String issuer;

    @Schema(description = "获奖时间", example = "2023-10-15")
    @Column(name = "award_time", length = 20)
    private String awardTime;

    @Schema(description = "荣誉描述", example = "在全国大学生编程大赛中表现优异，获得一等奖")
    @Column(name = "description", length = 1000)
    private String description;

    @Schema(description = "荣誉证书图片", example = "https://example.com/certificate.jpg")
    @Column(name = "certificate_image", length = 500)
    private String certificateImage;

    @Schema(description = "排序", example = "1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;
}
