package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 社团关注实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_follow")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团关注信息")
public class ClubFollow extends BaseEntity {

    @Schema(description = "用户ID", example = "1")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "社团名称", example = "计算机协会")
    @Column(name = "club_name", length = 100)
    private String clubName;

    @Schema(description = "社团Logo", example = "https://example.com/club-logo.png")
    @Column(name = "club_logo", length = 500)
    private String clubLogo;
}
