package com.medical.appointment.service;

import com.medical.appointment.entity.AuditLog;
import com.medical.appointment.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Transactional
    public AuditLog log(AuditLog.ActionType actionType, String actionDesc, 
                        Long userId, String userPhone,
                        String targetType, Long targetId) {
        AuditLog log = AuditLog.builder()
                .actionType(actionType)
                .actionDesc(actionDesc)
                .userPhone(userPhone)
                .targetType(targetType)
                .targetId(targetId)
                .requestUrl(request != null ? request.getRequestURI() : null)
                .requestMethod(request != null ? request.getMethod() : null)
                .clientIp(getClientIp())
                .userAgent(request != null ? request.getHeader("User-Agent") : null)
                .status(AuditLog.ResultStatus.SUCCESS)
                .build();
        
        if (userId != null) {
            com.medical.appointment.entity.User user = new com.medical.appointment.entity.User();
            user.setId(userId);
            log.setUser(user);
        }
        
        return auditLogRepository.save(log);
    }

    @Transactional
    public AuditLog logError(AuditLog.ActionType actionType, String actionDesc, 
                             Long userId, String userPhone,
                             String targetType, Long targetId, String errorMessage) {
        AuditLog log = log(actionType, actionDesc, userId, userPhone, targetType, targetId);
        log.setStatus(AuditLog.ResultStatus.FAILED);
        log.setErrorMessage(errorMessage);
        return auditLogRepository.save(log);
    }

    public void logLogin(Long userId, String userPhone, String loginType) {
        log(AuditLog.ActionType.LOGIN, "用户登录 - " + loginType, userId, userPhone, "User", userId);
    }

    public void logLogout(Long userId, String userPhone) {
        log(AuditLog.ActionType.LOGOUT, "用户退出登录", userId, userPhone, "User", userId);
    }

    public void logAddPatient(Long userId, String userPhone, Long patientId) {
        log(AuditLog.ActionType.ADD_PATIENT, "添加就诊人", userId, userPhone, "Patient", patientId);
    }

    public void logUpdatePatient(Long userId, String userPhone, Long patientId) {
        log(AuditLog.ActionType.UPDATE_PATIENT, "更新就诊人信息", userId, userPhone, "Patient", patientId);
    }

    public void logDeletePatient(Long userId, String userPhone, Long patientId) {
        log(AuditLog.ActionType.DELETE_PATIENT, "删除就诊人", userId, userPhone, "Patient", patientId);
    }

    public void logCreateAppointment(Long userId, String userPhone, Long appointmentId) {
        log(AuditLog.ActionType.CREATE_APPOINTMENT, "创建预约", userId, userPhone, "Appointment", appointmentId);
    }

    public void logCancelAppointment(Long userId, String userPhone, Long appointmentId) {
        log(AuditLog.ActionType.CANCEL_APPOINTMENT, "取消预约", userId, userPhone, "Appointment", appointmentId);
    }

    public void logPayAppointment(Long userId, String userPhone, Long appointmentId) {
        log(AuditLog.ActionType.PAY_APPOINTMENT, "预约支付", userId, userPhone, "Appointment", appointmentId);
    }

    public void logUpdateProfile(Long userId, String userPhone) {
        log(AuditLog.ActionType.UPDATE_PROFILE, "更新个人资料", userId, userPhone, "User", userId);
    }

    public void logVerifyIdentity(Long userId, String userPhone, Long patientId) {
        log(AuditLog.ActionType.VERIFY_IDENTITY, "实名认证", userId, userPhone, "Patient", patientId);
    }

    @Transactional
    public int cleanupOldLogs(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        return auditLogRepository.deleteLogsOlderThan(cutoffDate);
    }

    private String getClientIp() {
        if (request == null) return null;
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
