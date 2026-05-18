package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
@Schema(description = "产品信息")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "产品ID")
    private Long id;

    @Column(length = 100)
    @Schema(description = "产品名称")
    private String name;

    @Column(length = 200)
    @Schema(description = "产品副标题")
    private String subtitle;

    @Column(length = 500)
    @Schema(description = "产品主图URL")
    private String mainImage;

    @Column(length = 2000)
    @Schema(description = "产品视频URL")
    private String videoUrl;

    @Column(length = 1000)
    @Schema(description = "产品标签，逗号分隔")
    private String tags;

    @Schema(description = "原价（分）")
    private Long originalPrice;

    @Schema(description = "售价（分）")
    private Long price;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "销量")
    private Integer sales;

    @Column(length = 100)
    @Schema(description = "花材")
    private String flowerMaterial;

    @Column(length = 100)
    @Schema(description = "包装")
    private String packaging;

    @Column(length = 200)
    @Schema(description = "花语")
    private String flowerLanguage;

    @Column(length = 500)
    @Schema(description = "适用场景")
    private String applicableScene;

    @Column(length = 2000)
    @Schema(description = "养护指南")
    private String careGuide;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "产品详情（富文本）")
    private String detail;

    @Schema(description = "是否新品：0-否，1-是")
    private Integer isNew;

    @Schema(description = "是否热卖：0-否，1-是")
    private Integer isHot;

    @Schema(description = "是否推荐：0-否，1-是")
    private Integer isRecommend;

    @Schema(description = "是否可用积分兑换：0-否，1-是")
    private Integer pointExchange;

    @Schema(description = "兑换所需积分")
    private Integer exchangePoints;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态：0-下架，1-上架")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) status = 1;
        if (sortOrder == null) sortOrder = 0;
        if (stock == null) stock = 0;
        if (sales == null) sales = 0;
        if (isNew == null) isNew = 0;
        if (isHot == null) isHot = 0;
        if (isRecommend == null) isRecommend = 0;
        if (pointExchange == null) pointExchange = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
