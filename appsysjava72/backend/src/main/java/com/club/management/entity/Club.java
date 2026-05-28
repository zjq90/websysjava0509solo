package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社团实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("club")
public class Club extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 社团名称
     */
    private String name;

    /**
     * 社团logo
     */
    private String logo;

    /**
     * 封面图
     */
    private String coverImage;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 社团简介
     */
    private String description;

    /**
     * 社团宗旨
     */
    private String purpose;

    /**
     * 社团章程
     */
    private String rules;

    /**
     * 成员数
     */
    private Integer memberCount;

    /**
     * 最大成员数
     */
    private Integer maxMembers;

    /**
     * 社长ID
     */
    private Long presidentId;

    /**
     * 状态 0停用 1正常
     */
    private Integer status;
}
