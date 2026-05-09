package com.inventory.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 预警记录实体类
 * 功能：存储各种预警信息（近效期、温湿度异常等）
 */
@Entity
@Table(name = "alert")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "预警类型不能为空")
    @Size(min = 1, max = 50, message = "预警类型长度必须在1-50之间")
    private String alertType;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "预警级别不能为空")
    @Size(min = 1, max = 20, message = "预警级别长度必须在1-20之间")
    private String level;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "预警消息不能为空")
    @Size(min = 1, max = 500, message = "预警消息长度必须在1-500之间")
    private String message;

    @Column(length = 100)
    @Size(max = 100, message = "关联实体长度不能超过100")
    private String relatedEntity;

    private Long relatedEntityId;

    @Column(length = 20)
    @Size(max = 20, message = "状态长度不能超过20")
    private String status;

    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getRelatedEntity() { return relatedEntity; }
    public void setRelatedEntity(String relatedEntity) { this.relatedEntity = relatedEntity; }
    public Long getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(Long relatedEntityId) { this.relatedEntityId = relatedEntityId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
