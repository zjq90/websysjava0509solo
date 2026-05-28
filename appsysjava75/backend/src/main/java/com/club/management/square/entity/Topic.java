package com.club.management.square.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 话题实体类
 * 围绕社团生活、学习、就业创建热门话题
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "topic")
@EqualsAndHashCode(callSuper = true)
public class Topic extends BaseEntity {

    /**
     * 话题名称
     */
    private String name;

    /**
     * 话题封面
     */
    private String cover;

    /**
     * 话题分类 0-社团生活 1-学习交流 2-就业指导 3-兴趣爱好 4-其他
     */
    private Integer category;

    /**
     * 话题描述
     */
    private String description;

    /**
     * 创建用户ID
     */
    private Long creatorId;

    /**
     * 创建者姓名
     */
    private String creatorName;

    /**
     * 帖子数量
     */
    private Integer postCount;

    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 浏览数
     */
    private Integer viewCount;

    /**
     * 是否热门 0-否 1-是
     */
    private Integer isHot;

    /**
     * 是否置顶 0-否 1-是
     */
    private Integer isTop;

    /**
     * 状态 0-正常 1-禁用
     */
    private Integer status;
}
