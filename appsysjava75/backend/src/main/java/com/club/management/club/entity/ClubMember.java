package com.club.management.club.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社团成员实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "club_member")
@EqualsAndHashCode(callSuper = true)
public class ClubMember extends BaseEntity {

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 职位 0-普通成员 1-副部长 2-部长 3-副会长 4-会长
     */
    private Integer position;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 部门
     */
    private String department;

    /**
     * 入社时间
     */
    private String joinDate;

    /**
     * 状态 0-待审核 1-已加入 2-已退出 3-已拒绝
     */
    private Integer status;
}
