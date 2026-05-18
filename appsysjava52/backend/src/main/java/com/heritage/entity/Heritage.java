package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 文物实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "heritage")
public class Heritage extends BaseEntity {

    /**
     * 文物名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 文物编号
     */
    @Column(name = "code", unique = true, length = 50)
    private String code;

    /**
     * 文物类别：1-青铜器，2-陶瓷，3-书画，4-玉器，5-杂项
     */
    @Column(name = "category", nullable = false)
    private Integer category;

    /**
     * 年代
     */
    @Column(name = "period", length = 100)
    private String period;

    /**
     * 尺寸描述
     */
    @Column(name = "dimensions", length = 200)
    private String dimensions;

    /**
     * 材质
     */
    @Column(name = "material", length = 100)
    private String material;

    /**
     * 简介
     */
    @Column(name = "description", length = 2000)
    private String description;

    /**
     * 主图URL
     */
    @Column(name = "main_image", length = 500)
    private String mainImage;

    /**
     * 高清图片URL（多个用逗号分隔）
     */
    @Column(name = "detail_images", length = 2000)
    private String detailImages;

    /**
     * 3D模型URL
     */
    @Column(name = "model_3d", length = 500)
    private String model3d;

    /**
     * 全景图URL
     */
    @Column(name = "panorama_image", length = 500)
    private String panoramaImage;

    /**
     * 历史背景故事
     */
    @Column(name = "history_story", columnDefinition = "TEXT")
    private String historyStory;

    /**
     * 修复历程
     */
    @Column(name = "restoration_story", columnDefinition = "TEXT")
    private String restorationStory;

    /**
     * 语音讲解URL
     */
    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    /**
     * 出土地点（仅显示到县级）
     */
    @Column(name = "excavation_site", length = 200)
    private String excavationSite;

    /**
     * 传承信息
     */
    @Column(name = "provenance", length = 1000)
    private String provenance;

    /**
     * 估值
     */
    @Column(name = "estimated_value", precision = 15, scale = 2)
    private BigDecimal estimatedValue;

    /**
     * 是否公开：0-私有，1-公开
     */
    @Column(name = "is_public", nullable = false)
    private Integer isPublic = 1;

    /**
     * 收藏次数
     */
    @Column(name = "favorite_count", nullable = false)
    private Integer favoriteCount = 0;

    /**
     * 浏览次数
     */
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    /**
     * 状态：0-下架，1-上架
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}