package com.hospital.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预约挂号请求DTO
 * 
 * @author hospital
 * @version 1.0.0
 */
public class AppointmentDTO {

    /**
     * 医生ID
     */
    @NotNull(message = "请选择医生")
    private Long doctorId;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 预约日期（字符串格式，如：2024-01-01）
     */
    private String appointmentDate;

    /**
     * 预约时间（字符串格式，如：08:30）
     */
    private String appointmentTime;

    /**
     * 开始时间（完整LocalDateTime，用于内部处理）
     */
    private LocalDateTime startTime;

    /**
     * 结束时间（完整LocalDateTime，用于内部处理）
     */
    private LocalDateTime endTime;

    /**
     * 时段类型：MORNING-上午，AFTERNOON-下午
     */
    private String timeSlotType;

    /**
     * 时段序号
     */
    private Integer timeSlotNo;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 症状描述
     */
    private String symptoms;

    /**
     * 患者主诉
     */
    private String chiefComplaint;

    /**
     * 挂号费
     */
    private BigDecimal fee;

    /**
     * 备注
     */
    private String remark;

    public AppointmentDTO() {
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getChiefComplaint() {
        return chiefComplaint != null ? chiefComplaint : symptoms;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
