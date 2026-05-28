package com.club.management.resource.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源分享实体类
 * 社团成员上传分享学习资料、比赛经验、技能教程
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "resource_share")
@EqualsAndHashCode(callSuper = true)
public class ResourceShare extends BaseEntity {

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源封面
     */
    private String cover;

    /**
     * 资源分类 0-学习资料 1-比赛经验 2-技能教程 3-工具软件 4-其他
     */
    private Integer category;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 资源类型 0-文档 1-视频 2-音频 3-图片 4-压缩包 5-其他
     */
    private Integer resourceType;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 上传用户ID
     */
    private Long uploaderId;

    /**
     * 上传者姓名
     */
    private String uploaderName;

    /**
     * 上传者头像
     */
    private String uploaderAvatar;

    /**
     * 社团ID（社团专属资源）
     */
    private Long clubId;

    /**
     * 社团名称
     */
    private String clubName;

    /**
     * 是否公开 0-仅社团可见 1-全站可见
     */
    private Integer isPublic;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 预览图URL
     */
    private String previewUrl;

    /**
     * 标签，多个用逗号分隔
     */
    private String tags;

    /**
     * 下载数
     */
    private Integer downloadCount;

    /**
     * 浏览数
     */
    private Integer viewCount;

    /**
     * 收藏数
     */
    private Integer favoriteCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评分（1-5分）
     */
    private Double rating;

    /**
     * 评分人数
     */
    private Integer ratingCount;

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
