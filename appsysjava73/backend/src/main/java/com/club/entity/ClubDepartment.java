package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 社团部门实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_department")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团部门信息")
public class ClubDepartment extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "部门名称", example = "技术部")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Schema(description = "部门描述", example = "负责社团技术开发和技术培训")
    @Column(name = "description", length = 500)
    private String description;

    @Schema(description = "部门负责人ID", example = "1")
    @Column(name = "leader_id")
    private Long leaderId;

    @Schema(description = "部门负责人姓名", example = "张三")
    @Column(name = "leader_name", length = 50)
    private String leaderName;

    @Schema(description = "成员数量", example = "20")
    @Column(name = "member_count", nullable = false)
    private Integer memberCount = 0;

    @Schema(description = "排序", example = "1")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;
}
