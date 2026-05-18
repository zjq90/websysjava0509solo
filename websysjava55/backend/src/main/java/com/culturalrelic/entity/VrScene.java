package com.culturalrelic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * VR场景实体类
 * 支持用户"走进"虚拟博物馆观看文物
 */
@Data
@Entity
@Table(name = "vr_scene")
@EqualsAndHashCode(callSuper = true)
public class VrScene extends BaseEntity {

    /**
     * 场景编号
     */
    @Column(name = "scene_no", nullable = false, unique = true, length = 50)
    private String sceneNo;

    /**
     * 场景名称
     */
    @Column(name = "scene_name", nullable = false, length = 200)
    private String sceneName;

    /**
     * 场景类型：1-博物馆展厅，2-专题展馆，3-虚拟漫游，4-其他
     */
    @Column(name = "scene_type", nullable = false)
    private Integer sceneType;

    /**
     * 场景描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 3D场景文件路径
     */
    @Column(name = "scene_path", length = 500)
    private String scenePath;

    /**
     * 场景格式
     */
    @Column(name = "scene_format", length = 50)
    private String sceneFormat;

    /**
     * 场景缩略图URL
     */
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    /**
     * 全景图URL（360度全景）
     */
    @Column(name = "panorama_url", length = 500)
    private String panoramaUrl;

    /**
     * 包含的文物ID列表（JSON数组）
     */
    @Column(name = "relic_ids", columnDefinition = "TEXT")
    private String relicIds;

    /**
     * 包含的文物数量
     */
    @Column(name = "relic_count")
    private Integer relicCount = 0;

    /**
     * 热点配置（JSON格式，存储导览热点信息）
     */
    @Column(name = "hotspots", columnDefinition = "TEXT")
    private String hotspots;

    /**
     * 背景音乐URL
     */
    @Column(name = "bgm_url", length = 500)
    private String bgmUrl;

    /**
     * 语音导览URL
     */
    @Column(name = "audio_guide_url", length = 500)
    private String audioGuideUrl;

    /**
     * 是否支持VR设备：0-不支持，1-支持
     */
    @Column(name = "support_vr_device", nullable = false)
    private Integer supportVrDevice = 1;

    /**
     * 支持的VR设备类型（JSON数组）
     */
    @Column(name = "vr_device_types", columnDefinition = "TEXT")
    private String vrDeviceTypes;

    /**
     * 访问次数
     */
    @Column(name = "visit_count")
    private Long visitCount = 0L;

    /**
     * 平均停留时长（秒）
     */
    @Column(name = "avg_duration")
    private Integer avgDuration;

    /**
     * 状态：0-未发布，1-已发布，2-维护中
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 版本号
     */
    @Column(name = "version", length = 50)
    private String version = "1.0";
}
