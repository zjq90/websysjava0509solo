package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 社团成员实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("club_member")
public class ClubMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色 0普通成员 1副社长 2社长
     */
    private Integer role;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;
}
