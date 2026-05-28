package com.club.management.entity;

import com.club.management.entity.enums.SignInStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 签到记录实体类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sign_in")
public class SignIn extends BaseEntity {

    /**
     * 活动ID
     */
    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    /**
     * 报名记录ID
     */
    @Column(name = "registration_id", nullable = false)
    private Long registrationId;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 学号（冗余，便于查询）
     */
    @Column(name = "student_no", length = 20)
    private String studentNo;

    /**
     * 姓名（冗余，便于查询）
     */
    @Column(name = "real_name", length = 50)
    private String realName;

    /**
     * 院系（冗余，便于查询）
     */
    @Column(name = "department", length = 100)
    private String department;

    /**
     * 签到状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SignInStatus status = SignInStatus.NOT_SIGNED;

    /**
     * 签到时间
     */
    @Column(name = "sign_in_time")
    private LocalDateTime signInTime;

    /**
     * 签到IP地址
     */
    @Column(name = "sign_in_ip", length = 50)
    private String signInIp;

    /**
     * 签到设备信息
     */
    @Column(name = "sign_in_device", length = 200)
    private String signInDevice;

    /**
     * 签到位置（经纬度）
     */
    @Column(name = "sign_in_location", length = 100)
    private String signInLocation;

    /**
     * 签到方式（QR_CODE：二维码扫码，MANUAL：手动补签）
     */
    @Column(name = "sign_in_method", length = 20)
    private String signInMethod;

    /**
     * 补签原因
     */
    @Column(name = "make_up_reason", length = 500)
    private String makeUpReason;

    /**
     * 补签操作人ID
     */
    @Column(name = "make_up_by")
    private Long makeUpBy;

    /**
     * 补签时间
     */
    @Column(name = "make_up_time")
    private LocalDateTime makeUpTime;

    /**
     * 签到时使用的二维码token（用于验证）
     */
    @Column(name = "qr_token", length = 100)
    private String qrToken;
}
