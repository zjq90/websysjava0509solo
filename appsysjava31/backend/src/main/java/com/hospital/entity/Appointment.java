package com.hospital.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预约挂号实体类
 * 存储患者预约挂号信息
 * 
 * @author hospital
 * @version 1.0.0
 */
@Entity
@Table(name = "biz_appointment")
public class Appointment {

    /**
     * 预约ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 预约编号
     */
    @Column(unique = true, nullable = false, length = 50)
    private String appointmentNo;

    /**
     * 患者用户ID
     */
    @Column(nullable = false)
    private Long patientId;

    /**
     * 医生ID
     */
    @Column(nullable = false)
    private Long doctorId;

    /**
     * 科室ID
     */
    @Column(nullable = false)
    private Long deptId;

    /**
     * 预约日期
     */
    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    /**
     * 预约开始时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 预约结束时间
     */
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * 时段类型：MORNING-上午，AFTERNOON-下午，EVENING-晚上
     */
    @Column(length = 20)
    private String timeSlotType;

    /**
     * 时段序号
     */
    private Integer timeSlotNo;

    /**
     * 挂号费用
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    /**
     * 支付状态：0-待支付，1-已支付，2-已退款
     */
    @Column(nullable = false)
    private Integer paymentStatus = 0;

    /**
     * 支付方式：WECHAT-微信，ALIPAY-支付宝，CARD-银行卡
     */
    @Column(length = 20)
    private String paymentMethod;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 支付流水号
     */
    @Column(length = 100)
    private String paymentNo;

    /**
     * 预约状态：PENDING-待就诊，CONFIRMED-已确认，COMPLETED-已完成，CANCELLED-已取消，NO_SHOW-爽约
     */
    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    /**
     * 就诊室号
     */
    @Column(length = 50)
    private String roomNo;

    /**
     * 排队序号
     */
    private Integer queueNo;

    /**
     * 患者主诉
     */
    @Column(length = 500)
    private String chiefComplaint;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 取消原因
     */
    @Column(length = 200)
    private String cancelReason;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 是否发送预约成功通知
     */
    private Integer notifiedSuccess = 0;

    /**
     * 是否发送就诊提醒
     */
    private Integer notifiedReminder = 0;

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

    public Appointment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTimeSlotType() {
        return timeSlotType;
    }

    public void setTimeSlotType(String timeSlotType) {
        this.timeSlotType = timeSlotType;
    }

    public Integer getTimeSlotNo() {
        return timeSlotNo;
    }

    public void setTimeSlotNo(Integer timeSlotNo) {
        this.timeSlotNo = timeSlotNo;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(Integer queueNo) {
        this.queueNo = queueNo;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public LocalDateTime getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(LocalDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Integer getNotifiedSuccess() {
        return notifiedSuccess;
    }

    public void setNotifiedSuccess(Integer notifiedSuccess) {
        this.notifiedSuccess = notifiedSuccess;
    }

    public Integer getNotifiedReminder() {
        return notifiedReminder;
    }

    public void setNotifiedReminder(Integer notifiedReminder) {
        this.notifiedReminder = notifiedReminder;
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
