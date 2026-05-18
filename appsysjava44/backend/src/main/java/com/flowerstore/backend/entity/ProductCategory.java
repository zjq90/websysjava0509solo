package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 产品分类实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_category")
@Schema(description = "产品分类")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分类ID")
    private Long id;

    @Schema(description = "父分类ID，0表示顶级分类")
    private Long parentId;

    @Column(length = 50)
    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类图标URL")
    private String icon;

    @Column(length = 200)
    @Schema(description = "分类描述")
    private String description;

    @Column(length = 20)
    @Schema(description = "分类类型：scene-场景分类，material-花材分类，price-价格区间")
    private String categoryType;

    @Schema(description = "最低价格（分），用于价格区间分类")
    private Long minPrice;

    @Schema(description = "最高价格（分），用于价格区间分类")
    private Long maxPrice;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态：0-禁用，1-启用")
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
        if (parentId == null) parentId = 0L;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
