package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 场地实体类
 * 用于管理影棚、外景地等场地信息
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "venue")
@Schema(description = "场地信息")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "场地ID", example = "1")
    private Long id;

    @NotBlank(message = "场地名称不能为空")
    @Column(nullable = false, length = 100)
    @Schema(description = "场地名称", example = "A棚-海景风格")
    private String name;

    @NotBlank(message = "场地编号不能为空")
    @Column(unique = true, nullable = false, length = 20)
    @Schema(description = "场地编号", example = "VEN001")
    private String venueNo;

    @NotNull(message = "场地类型不能为空")
    @Column(nullable = false, length = 20)
    @Schema(description = "场地类型", example = "STUDIO")
    @Enumerated(EnumType.STRING)
    private VenueType type;

    @Column(length = 200)
    @Schema(description = "地址", example = "北京市朝阳区XX路XX号")
    private String address;

    @Column
    @Schema(description = "容纳人数", example = "10")
    private Integer capacity;

    @Column(length = 500)
    @Schema(description = "描述")
    private String description;

    @Column(nullable = false)
    @Schema(description = "是否可用", example = "true")
    private Boolean available = true;

    @Column(length = 20)
    @Schema(description = "状态", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private VenueStatus status = VenueStatus.ACTIVE;

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
     * 场地类型枚举
     */
    public enum VenueType {
        STUDIO("影棚"),
        OUTDOOR("外景地"),
        DRESSING_ROOM("化妆间"),
        REST_AREA("休息区");

        private final String description;

        VenueType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 场地状态枚举
     */
    public enum VenueStatus {
        ACTIVE("正常使用"),
        MAINTENANCE("维护中"),
        CLOSED("已关闭");

        private final String description;

        VenueStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
