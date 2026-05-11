package com.hospital.dto;

import java.math.BigDecimal;

/**
 * 医生信息返回VO
 * 用于前端展示
 * 
 * @author hospital
 * @version 1.0.0
 */
public class DoctorVO {

    private Long id;
    private Long deptId;
    private String name;
    private String doctorName;
    private String title;
    private String education;
    private Integer experienceYears;
    private String specialties;
    private String specialty;
    private String introduction;
    private BigDecimal fee;
    private BigDecimal registrationFee;
    private String avatar;
    private BigDecimal rating;
    private Integer consultationCount;
    private Integer isRecommended;
    private Integer status;
    private String departmentName;
    private String hospital;

    public DoctorVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name != null ? name : doctorName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoctorName() {
        return doctorName != null ? doctorName : name;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getSpecialties() {
        return specialties != null ? specialties : specialty;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }

    public String getSpecialty() {
        return specialty != null ? specialty : specialties;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public BigDecimal getFee() {
        return fee != null ? fee : registrationFee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getRegistrationFee() {
        return registrationFee != null ? registrationFee : fee;
    }

    public void setRegistrationFee(BigDecimal registrationFee) {
        this.registrationFee = registrationFee;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getConsultationCount() {
        return consultationCount;
    }

    public void setConsultationCount(Integer consultationCount) {
        this.consultationCount = consultationCount;
    }

    public Integer getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Integer isRecommended) {
        this.isRecommended = isRecommended;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
}
