package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 个人收藏实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "collection_item")
public class CollectionItem extends BaseEntity {

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 文物ID（系统文物）
     */
    @Column(name = "heritage_id")
    private Long heritageId;

    /**
     * 自定义文物名称（用户自己添加的文物）
     */
    @Column(name = "custom_name", length = 200)
    private String customName;

    /**
     * 自定义文物描述
     */
    @Column(name = "custom_description", length = 2000)
    private String customDescription;

    /**
     * 自定义文物图片URL
     */
    @Column(name = "custom_images", length = 2000)
    private String customImages;

    /**
     * 类别
     */
    @Column(name = "category")
    private Integer category;

    /**
     * 标签（多个用逗号分隔）
     */
    @Column(name = "tags", length = 500)
    private String tags;

    /**
     * 材质
     */
    @Column(name = "material", length = 100)
    private String material;

    /**
     * 入藏时间
     */
    @Column(name = "collection_date")
    private java.time.LocalDate collectionDate;

    /**
     * 估值
     */
    @Column(name = "estimated_value", precision = 15, scale = 2)
    private java.math.BigDecimal estimatedValue;

    /**
     * 备注
     */
    @Column(name = "remark", length = 1000)
    private String remark;

    /**
     * 是否启用环境监测：0-不启用，1-启用
     */
    @Column(name = "env_monitor_enabled", nullable = false)
    private Integer envMonitorEnabled = 0;

    /**
     * 关联的物联网设备ID
     */
    @Column(name = "device_id", length = 100)
    private String deviceId;
}