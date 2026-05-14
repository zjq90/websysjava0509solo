package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐实体类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "package")
@Schema(description = "套餐信息")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "套餐ID", example = "1")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "套餐名称", example = "轻奢婚纱套餐")
    private String name;

    @Column(name = "type", length = 50)
    @Schema(description = "套餐类型", example = "婚纱/写真/儿童/全家福")
    private String type;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @Schema(description = "套餐价格", example = "5999.00")
    private BigDecimal price;

    @Column(name = "description", length = 1000)
    @Schema(description = "套餐描述", example = "包含3套服装、100张底片、40张精修...")
    private String description;

    @Column(name = "photo_count")
    @Schema(description = "拍摄张数", example = "100")
    private Integer photoCount;

    @Column(name = "retouch_count")
    @Schema(description = "精修张数", example = "40")
    private Integer retouchCount;

    @Column(name = "album_count")
    @Schema(description = "相册数量", example = "2")
    private Integer albumCount;

    @Column(name = "frame_count")
    @Schema(description = "相框数量", example = "3")
    private Integer frameCount;

    @Column(name = "shooting_days")
    @Schema(description = "拍摄天数", example = "1")
    private Integer shootingDays;

    @Column(name = "clothing_sets")
    @Schema(description = "服装套数", example = "3")
    private Integer clothingSets;

    @Column(name = "makeup_times")
    @Schema(description = "化妆次数", example = "3")
    private Integer makeupTimes;

    @Column(name = "status")
    @Schema(description = "状态 0-下架 1-上架", example = "1")
    private Integer status;

    @Column(name = "sort_order")
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @Column(name = "cover_image", length = 255)
    @Schema(description = "封面图片")
    private String coverImage;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = 1;
        }
        if (sortOrder == null) {
            sortOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
