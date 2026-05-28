package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 历任社长实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_past_president")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "历任社长信息")
public class PastPresident extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "用户ID", example = "1")
    @Column(name = "user_id")
    private Long userId;

    @Schema(description = "姓名", example = "张三")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Schema(description = "头像", example = "https://example.com/avatar.png")
    @Column(name = "avatar", length = 500)
    private String avatar;

    @Schema(description = "学号", example = "2018001001")
    @Column(name = "student_no", length = 50)
    private String studentNo;

    @Schema(description = "任期开始时间", example = "2020-09-01")
    @Column(name = "term_start", length = 20)
    private String termStart;

    @Schema(description = "任期结束时间", example = "2021-08-31")
    @Column(name = "term_end", length = 20)
    private String termEnd;

    @Schema(description = "任期描述", example = "在任期间成功举办了多次大型活动，社团成员规模扩大一倍")
    @Column(name = "description", length = 1000)
    private String description;

    @Schema(description = "排序", example = "1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;
}
