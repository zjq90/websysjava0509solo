package com.appsys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "通知消息实体")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "通知ID")
    private Long id;

    @Column(name = "user_id")
    @Schema(description = "用户ID（为空表示全体用户）")
    private Long userId;

    @Column(nullable = false, length = 100)
    @Schema(description = "通知标题")
    private String title;

    @Column(length = 1000)
    @Schema(description = "通知内容")
    private String content;

    @Column(nullable = false)
    @Schema(description = "通知类型：1-账单提醒，2-服务进度更新，3-优惠活动，4-安全预警")
    private Integer type;

    @Column(length = 255)
    @Schema(description = "跳转链接")
    private String linkUrl;

    @Column(nullable = false)
    @Schema(description = "是否已读")
    private Boolean isRead = false;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
