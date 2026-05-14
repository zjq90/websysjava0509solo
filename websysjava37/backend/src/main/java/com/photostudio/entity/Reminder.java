package com.photostudio.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 提醒实体类
 * 用于纪念日提醒、节点提醒等
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Entity
@Table(name = "reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "提醒信息")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "提醒ID")
    private Long id;

    @NotNull(message = "客户ID不能为空")
    @Column(name = "customer_id", nullable = false)
    @Schema(description = "客户ID")
    private Long customerId;

    @NotBlank(message = "提醒标题不能为空")
    @Column(nullable = false, length = 100)
    @Schema(description = "提醒标题", example = "结婚周年纪念")
    private String title;

    @Column(length = 500)
    @Schema(description = "提醒内容", example = "客户结婚三周年纪念日，发送祝福信息")
    private String content;

    @NotNull(message = "提醒时间不能为空")
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "提醒时间")
    private LocalDateTime reminderTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "提醒类型")
    private ReminderType type;

    @Column(nullable = false)
    @Schema(description = "是否已处理")
    private Boolean processed = false;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Column(nullable = false)
    @Schema(description = "是否删除")
    private Boolean deleted = false;

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
     * 提醒类型枚举
     */
    public enum ReminderType {
        SHOOTING_BEFORE("拍摄前提醒"),
        PICKUP_AFTER("取件后回访"),
        ANNIVERSARY("纪念日提醒"),
        FOLLOW_UP("跟进提醒"),
        MARKETING("营销提醒"),
        OTHER("其他提醒");

        private final String description;

        ReminderType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
