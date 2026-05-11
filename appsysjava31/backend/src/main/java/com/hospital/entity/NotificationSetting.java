package com.hospital.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 通知设置实体类
 * 存储用户通知偏好设置
 * 
 * @author hospital
 * @version 1.0.0
 */
@Entity
@Table(name = "sys_notification_setting")
public class NotificationSetting {

    /**
     * 设置ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(nullable = false, unique = true)
    private Long userId;

    /**
     * 预约成功通知：1-开启，0-关闭
     */
    @Column(nullable = false)
    private Integer appointmentSuccess = 1;

    /**
     * 就诊提醒通知：1-开启，0-关闭
     */
    @Column(nullable = false)
    private Integer appointmentReminder = 1;

    /**
     * 医生停诊通知：1-开启，0-关闭
     */
    @Column(nullable = false)
    private Integer doctorCancel = 1;

    /**
     * 报告就绪通知：1-开启，0-关闭
     */
    @Column(nullable = false)
    private Integer reportReady = 1;

    /**
     * 系统通知：1-开启，0-关闭
     */
    @Column(nullable = false)
    private Integer systemNotice = 1;

    /**
     * 支付相关通知：1-开启，0-关闭
     */
    @Column(nullable = false)
    private Integer paymentNotice = 1;

    /**
     * 就诊提醒提前时间（分钟）：默认60分钟
     */
    private Integer reminderMinutes = 60;

    /**
     * 消息推送方式：IN_APP-站内消息，WECHAT-微信，ALL-全部
     */
    @Column(length = 20)
    private String pushChannel = "ALL";

    /**
     * 消息免打扰：1-开启，0-关闭
     */
    private Integer doNotDisturb = 0;

    /**
     * 免打扰开始时间
     */
    @Column(length = 10)
    private String dndStart = "22:00";

    /**
     * 免打扰结束时间
     */
    @Column(length = 10)
    private String dndEnd = "07:00";

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
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

    public NotificationSetting() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAppointmentSuccess() {
        return appointmentSuccess;
    }

    public void setAppointmentSuccess(Integer appointmentSuccess) {
        this.appointmentSuccess = appointmentSuccess;
    }

    public Integer getAppointmentReminder() {
        return appointmentReminder;
    }

    public void setAppointmentReminder(Integer appointmentReminder) {
        this.appointmentReminder = appointmentReminder;
    }

    public Integer getDoctorCancel() {
        return doctorCancel;
    }

    public void setDoctorCancel(Integer doctorCancel) {
        this.doctorCancel = doctorCancel;
    }

    public Integer getReportReady() {
        return reportReady;
    }

    public void setReportReady(Integer reportReady) {
        this.reportReady = reportReady;
    }

    public Integer getSystemNotice() {
        return systemNotice;
    }

    public void setSystemNotice(Integer systemNotice) {
        this.systemNotice = systemNotice;
    }

    public Integer getPaymentNotice() {
        return paymentNotice;
    }

    public void setPaymentNotice(Integer paymentNotice) {
        this.paymentNotice = paymentNotice;
    }

    public Integer getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(Integer reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }

    public String getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(String pushChannel) {
        this.pushChannel = pushChannel;
    }

    public Integer getDoNotDisturb() {
        return doNotDisturb;
    }

    public void setDoNotDisturb(Integer doNotDisturb) {
        this.doNotDisturb = doNotDisturb;
    }

    public String getDndStart() {
        return dndStart;
    }

    public void setDndStart(String dndStart) {
        this.dndStart = dndStart;
    }

    public String getDndEnd() {
        return dndEnd;
    }

    public void setDndEnd(String dndEnd) {
        this.dndEnd = dndEnd;
    }

    public Integer getVisitReminder() {
        return appointmentReminder;
    }

    public void setVisitReminder(Integer visitReminder) {
        this.appointmentReminder = visitReminder;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
