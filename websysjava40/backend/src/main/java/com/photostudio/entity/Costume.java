package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 服装实体类
 * 用于管理婚纱、主题服装等服装库存和状态
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "costume")
@Schema(description = "服装信息")
public class Costume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "服装ID", example = "1")
    private Long id;

    @NotBlank(message = "服装名称不能为空")
    @Column(nullable = false, length = 100)
    @Schema(description = "服装名称", example = "白色抹胸婚纱")
    private String name;

    @NotBlank(message = "服装编号不能为空")
    @Column(unique = true, nullable = false, length = 20)
    @Schema(description = "服装编号", example = "COS001")
    private String costumeNo;

    @NotNull(message = "服装类型不能为空")
    @Column(nullable = false, length = 50)
    @Schema(description = "服装类型", example = "WEDDING_DRESS")
    @Enumerated(EnumType.STRING)
    private CostumeType type;

    @Column(length = 20)
    @Schema(description = "尺码", example = "M")
    private String size;

    @Column(length = 30)
    @Schema(description = "颜色", example = "白色")
    private String color;

    @Column
    @Schema(description = "使用次数", example = "10")
    private Integer useCount = 0;

    @Column(nullable = false, length = 20)
    @Schema(description = "清洗状态", example = "CLEAN")
    @Enumerated(EnumType.STRING)
    private CleaningStatus cleaningStatus = CleaningStatus.CLEAN;

    @Column(length = 500)
    @Schema(description = "描述")
    private String description;

    @Column(nullable = false)
    @Schema(description = "是否可用", example = "true")
    private Boolean available = true;

    @Column
    @Schema(description = "上次清洗时间")
    private LocalDateTime lastCleanTime;

    @Column
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    /**
     * 服装类型枚举
     */
    public enum CostumeType {
        WEDDING_DRESS("婚纱"),
        SUIT("西装"),
        EVENING_DRESS("晚礼服"),
        CHINESE_STYLE("中式服装"),
        THEME_COSTUME("主题服装"),
        ACCESSORY("配饰");

        private final String description;

        CostumeType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 清洗状态枚举
     */
    public enum CleaningStatus {
        CLEAN("已清洁"),
        TO_BE_CLEANED("待清洗"),
        CLEANING("清洗中");

        private final String description;

        CleaningStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
