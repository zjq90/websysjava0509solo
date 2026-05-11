package com.hospital.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 消息通知实体类
 * 存储系统消息和通知信息
 * 
 * @author hospital
 * @version 1.0.0
 */
@Entity
@Table(name = "biz_message")
public class Message {

    /**
     * 消息ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 消息类型：APPOINTMENT_SUCCESS-预约成功，APPOINTMENT_REMINDER-就诊提醒，DOCTOR_CANCEL-医生停诊，REPORT_READY-报告就绪，SYSTEM-系统通知
     */
    @Column(nullable = false, length = 50)
    private String messageType;

    /**
     * 接收用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 消息标题
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 消息内容
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 关联业务ID（如预约ID、报告ID等）
     */
    private Long businessId;

    /**
     * 业务类型：APPOINTMENT-预约，REPORT-报告，INVOICE-票据
     */
    @Column(length = 20)
    private String businessType;

    /**
     * 消息模板ID
     */
    @Column(length = 50)
    private String templateId;

    /**
     * 跳转链接
     */
    @Column(length = 500)
    private String redirectUrl;

    /**
     * 是否已读：0-未读，1-已读
     */
    @Column(nullable = false)
    private Integer isRead = 0;

    /**
     * 已读时间
     */
    private LocalDateTime readTime;

    /**
     * 发送渠道：IN_APP-站内消息，WECHAT-微信消息，SMS-短信，EMAIL-邮件
     */
    @Column(length = 20)
    private String channel = "IN_APP";

    /**
     * 发送状态：PENDING-待发送，SENT-已发送，FAILED-发送失败
     */
    @Column(length = 20)
    private String sendStatus = "PENDING";

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 发送失败原因
     */
    @Column(length = 500)
    private String failReason;

    /**
     * 预约发送时间（用于延迟发送）
     */
    private LocalDateTime scheduledTime;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
