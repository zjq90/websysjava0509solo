package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * DIY花束实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diy_bouquet")
@Schema(description = "DIY花束")
public class DIYBouquet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "DIY花束ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Column(length = 100)
    @Schema(description = "花束名称")
    private String name;

    @Column(length = 500)
    @Schema(description = "主花材，JSON格式存储")
    private String mainFlowers;

    @Column(length = 500)
    @Schema(description = "配花材，JSON格式存储")
    private String secondaryFlowers;

    @Column(length = 500)
    @Schema(description = "配花，JSON格式存储")
    private String sideFlowers;

    @Column(length = 200)
    @Schema(description = "包装纸颜色")
    private String wrapperColor;

    @Column(length = 200)
    @Schema(description = "包装颜色")
    private String packagingColor;

    @Column(length = 200)
    @Schema(description = "包装样式")
    private String packagingStyle;

    @Column(length = 200)
    @Schema(description = "丝带颜色")
    private String ribbonColor;

    @Column(length = 500)
    @Schema(description = "贺卡内容")
    private String cardContent;

    @Column(length = 500)
    @Schema(description = "留言卡片")
    private String greetingCard;

    @Column(length = 1000)
    @Schema(description = "备注")
    private String remarks;

    @Column(length = 1000)
    @Schema(description = "预览图片URL，多个用逗号分隔")
    private String previewImages;

    @Schema(description = "预估价格（分）")
    private Long estimatedPrice;

    @Schema(description = "状态：0-草稿，1-已保存，2-已下单")
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
        if (status == null) status = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
