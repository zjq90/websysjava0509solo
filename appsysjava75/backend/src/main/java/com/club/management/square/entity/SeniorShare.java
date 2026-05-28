package com.club.management.square.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 学长分享实体类
 * 毕业校友、优秀学长分享社团经验、就业建议
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "senior_share")
@EqualsAndHashCode(callSuper = true)
public class SeniorShare extends BaseEntity {

    /**
     * 分享标题
     */
    private String title;

    /**
     * 分享封面
     */
    private String cover;

    /**
     * 分类 0-社团经验 1-就业指导 2-考研留学 3-技能学习 4-生活感悟
     */
    private Integer category;

    /**
     * 分享内容
     */
    private String content;

    /**
     * 作者ID（学长ID）
     */
    private Long authorId;

    /**
     * 作者姓名
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 作者简介
     */
    private String authorBio;

    /**
     * 毕业年份
     */
    private String graduateYear;

    /**
     * 工作单位
     */
    private String company;

    /**
     * 职位
     */
    private String position;

    /**
     * 原社团
     */
    private String originalClub;

    /**
     * 图片URL，多个用逗号分隔
     */
    private String images;

    /**
     * 附件URL
     */
    private String attachmentUrl;

    /**
     * 附件名称
     */
    private String attachmentName;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 收藏数
     */
    private Integer favoriteCount;

    /**
     * 浏览数
     */
    private Integer viewCount;

    /**
     * 是否精华 0-否 1-是
     */
    private Integer isEssence;

    /**
     * 是否置顶 0-否 1-是
     */
    private Integer isTop;

    /**
     * 状态 0-正常 1-已删除 2-审核中
     */
    private Integer status;
}
