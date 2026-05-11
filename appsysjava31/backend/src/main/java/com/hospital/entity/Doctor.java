package com.hospital.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医生实体类
 * 存储医生详细信息
 * 
 * @author hospital
 * @version 1.0.0
 */
@Entity
@Table(name = "sys_doctor")
public class Doctor {

    /**
     * 医生ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 关联科室ID
     */
    @Column(nullable = false)
    private Long deptId;

    /**
     * 医生编号
     */
    @Column(unique = true, nullable = false, length = 20)
    private String doctorCode;

    /**
     * 医生姓名
     */
    @Column(nullable = false, length = 100)
    private String doctorName;

    /**
     * 职称：PROFESSOR-主任医师，ASSOCIATE_PROFESSOR-副主任医师，ATTENDING-主治医师，RESIDENT-住院医师
     */
    @Column(length = 50)
    private String title;

    /**
     * 学历：DOCTOR-博士，MASTER-硕士，BACHELOR-本科
     */
    @Column(length = 20)
    private String education;

    /**
     * 从业年限
     */
    private Integer experienceYears;

    /**
     * 擅长领域
     */
    @Column(length = 500)
    private String specialty;

    /**
     * 医生简介
     */
    @Column(length = 1000)
    private String introduction;

    /**
     * 挂号费
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal registrationFee;

    /**
     * 医生头像
     */
    @Column(length = 500)
    private String avatar;

    /**
     * 评分（1-5星）
     */
    private BigDecimal rating;

    /**
     * 接诊数量
     */
    private Integer consultationCount = 0;

    /**
     * 是否推荐：1-是，0-否
     */
    private Integer isRecommended = 0;

    /**
     * 状态：1-在职，0-离职，2-停诊
     */
    @Column(nullable = false)
    private Integer status = 1;

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

    public Doctor() {
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getDoctorName() {
        return doctorName;
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

    public String getSpecialty() {
        return specialty;
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

    public BigDecimal getRegistrationFee() {
        return registrationFee;
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
