package com.culturalrelic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 文物数字分身实体类
 * 支持虚拟修复演示
 */
@Data
@Entity
@Table(name = "digital_twin")
@EqualsAndHashCode(callSuper = true)
public class DigitalTwin extends BaseEntity {

    /**
     * 关联的文物ID
     */
    @Column(name = "relic_id", nullable = false)
    private Long relicId;

    /**
     * 文物名称
     */
    @Column(name = "relic_name", nullable = false, length = 200)
    private String relicName;

    /**
     * 数字分身名称
     */
    @Column(name = "twin_name", nullable = false, length = 200)
    private String twinName;

    /**
     * 3D模型文件路径
     */
    @Column(name = "model_path", length = 500)
    private String modelPath;

    /**
     * 模型格式：obj, fbx, glb, gltf等
     */
    @Column(name = "model_format", length = 50)
    private String modelFormat;

    /**
     * 模型大小（MB）
     */
    @Column(name = "model_size")
    private Double modelSize;

    /**
     * 纹理贴图路径
     */
    @Column(name = "texture_path", length = 500)
    private String texturePath;

    /**
     * 原始状态模型路径
     */
    @Column(name = "original_model_path", length = 500)
    private String originalModelPath;

    /**
     * 修复后模型路径
     */
    @Column(name = "restored_model_path", length = 500)
    private String restoredModelPath;

    /**
     * 修复演示动画路径
     */
    @Column(name = "restoration_animation_path", length = 500)
    private String restorationAnimationPath;

    /**
     * 损伤部位标记（JSON格式）
     */
    @Column(name = "damage_marks", columnDefinition = "TEXT")
    private String damageMarks;

    /**
     * 修复方案描述
     */
    @Column(name = "restoration_plan", columnDefinition = "TEXT")
    private String restorationPlan;

    /**
     * 修复步骤（JSON数组）
     */
    @Column(name = "restoration_steps", columnDefinition = "TEXT")
    private String restorationSteps;

    /**
     * 模型精度等级：1-低，2-中，3-高，4-超高
     */
    @Column(name = "precision_level")
    private Integer precisionLevel = 2;

    /**
     * 多边形数量
     */
    @Column(name = "polygon_count")
    private Long polygonCount;

    /**
     * 是否支持虚拟修复演示：0-不支持，1-支持
     */
    @Column(name = "support_restoration_demo", nullable = false)
    private Integer supportRestorationDemo = 0;

    /**
     * 状态：0-创建中，1-已完成，2-更新中
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 版本号
     */
    @Column(name = "version", length = 50)
    private String version = "1.0";

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
