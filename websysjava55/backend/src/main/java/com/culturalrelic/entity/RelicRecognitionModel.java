package com.culturalrelic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 文物识别模型实体类
 * 部署AI模型自动识别文物类别
 */
@Data
@Entity
@Table(name = "relic_recognition_model")
@EqualsAndHashCode(callSuper = true)
public class RelicRecognitionModel extends BaseEntity {

    /**
     * 模型编号
     */
    @Column(name = "model_no", nullable = false, unique = true, length = 50)
    private String modelNo;

    /**
     * 模型名称
     */
    @Column(name = "model_name", nullable = false, length = 200)
    private String modelName;

    /**
     * 模型版本
     */
    @Column(name = "model_version", nullable = false, length = 50)
    private String modelVersion;

    /**
     * 模型类型：1-图像分类，2-目标检测，3-特征提取，4-其他
     */
    @Column(name = "model_type", nullable = false)
    private Integer modelType;

    /**
     * 模型文件路径
     */
    @Column(name = "model_path", length = 500)
    private String modelPath;

    /**
     * 模型框架：tensorflow, pytorch, onnx等
     */
    @Column(name = "framework", length = 50)
    private String framework;

    /**
     * 模型大小（MB）
     */
    @Column(name = "model_size")
    private Double modelSize;

    /**
     * 支持识别的类别（JSON数组）
     */
    @Column(name = "supported_categories", columnDefinition = "TEXT")
    private String supportedCategories;

    /**
     * 类别数量
     */
    @Column(name = "category_count")
    private Integer categoryCount;

    /**
     * 准确率（百分比）
     */
    @Column(name = "accuracy")
    private Double accuracy;

    /**
     * 召回率（百分比）
     */
    @Column(name = "recall")
    private Double recall;

    /**
     * F1分数
     */
    @Column(name = "f1_score")
    private Double f1Score;

    /**
     * 推理速度（毫秒/张）
     */
    @Column(name = "inference_speed")
    private Double inferenceSpeed;

    /**
     * 输入图像宽度
     */
    @Column(name = "input_width")
    private Integer inputWidth = 224;

    /**
     * 输入图像高度
     */
    @Column(name = "input_height")
    private Integer inputHeight = 224;

    /**
     * 预处理配置（JSON）
     */
    @Column(name = "preprocess_config", columnDefinition = "TEXT")
    private String preprocessConfig;

    /**
     * 后处理配置（JSON）
     */
    @Column(name = "postprocess_config", columnDefinition = "TEXT")
    private String postprocessConfig;

    /**
     * 标签映射文件路径
     */
    @Column(name = "label_map_path", length = 500)
    private String labelMapPath;

    /**
     * 训练数据集描述
     */
    @Column(name = "training_dataset", columnDefinition = "TEXT")
    private String trainingDataset;

    /**
     * 训练时间
     */
    @Column(name = "training_time")
    private String trainingTime;

    /**
     * 状态：0-未部署，1-已部署，2-部署中，3-已禁用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 是否默认模型：0-否，1-是
     */
    @Column(name = "is_default", nullable = false)
    private Integer isDefault = 0;

    /**
     * 推理次数
     */
    @Column(name = "inference_count")
    private Long inferenceCount = 0L;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
