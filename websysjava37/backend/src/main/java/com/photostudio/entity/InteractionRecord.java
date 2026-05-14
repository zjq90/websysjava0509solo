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
 * 客户互动记录实体类
 * 用于记录与客户的每次沟通内容
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Entity
@Table(name = "interaction_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "客户互动记录")
public class InteractionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @NotNull(message = "客户ID不能为空")
    @Column(name = "customer_id", nullable = false)
    @Schema(description = "客户ID")
    private Long customerId;

    @NotBlank(message = "沟通内容不能为空")
    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "沟通内容", example = "客户咨询婚纱照套餐，表示对森系风格感兴趣")
    private String content;

    @Column(length = 50)
    @Schema(description = "跟进人", example = "王经理")
    private String followUpPerson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowUpTime;

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
}
