package com.culturalrelic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 文物实体类
 * 存储文物的基本信息
 */
@Data
@Entity
@Table(name = "cultural_relic")
@EqualsAndHashCode(callSuper = true)
public class CulturalRelic extends BaseEntity {

    /**
     * 文物编号
     */
    @Column(name = "relic_no", nullable = false, unique = true, length = 50)
    private String relicNo;

    /**
     * 文物名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 文物类别（如：瓷器、青铜器、书画等）
     */
    @Column(name = "category", length = 100)
    private String category;

    /**
     * 朝代
     */
    @Column(name = "dynasty", length = 100)
    private String dynasty;

    /**
     * 年代描述
     */
    @Column(name = "period", length = 200)
    private String period;

    /**
     * 材质
     */
    @Column(name = "material", length = 100)
    private String material;

    /**
     * 尺寸描述
     */
    @Column(name = "size", length = 200)
    private String size;

    /**
     * 重量（克）
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * 文物描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 文物状态：0-正常，1-修复中，2-展览中，3-封存
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 存放位置
     */
    @Column(name = "location", length = 200)
    private String location;

    /**
     * 图片URL
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * 来源
     */
    @Column(name = "source", length = 200)
    private String source;

    /**
     * 入藏时间
     */
    @Column(name = "collect_date")
    private String collectDate;

    /**
     * 负责人
     */
    @Column(name = "responsible_person", length = 100)
    private String responsiblePerson;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
}
