package com.medical.appointment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_phone", length = 20)
    private String userPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", length = 30, nullable = false)
    private ActionType actionType;

    @Column(name = "action_desc", length = 500)
    private String actionDesc;

    @Column(name = "target_type", length = 30)
    private String targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "request_url", length = 500)
    private String requestUrl;

    @Column(name = "request_method", length = 10)
    private String requestMethod;

    @Column(name = "client_ip", length = 50)
    private String clientIp;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(columnDefinition = "TEXT")
    private String params;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @Builder.Default
    private ResultStatus status = ResultStatus.SUCCESS;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }

    public enum ActionType {
        LOGIN,
        LOGOUT,
        REGISTER,
        VERIFY_IDENTITY,
        ADD_PATIENT,
        UPDATE_PATIENT,
        DELETE_PATIENT,
        CREATE_APPOINTMENT,
        CANCEL_APPOINTMENT,
        PAY_APPOINTMENT,
        REFUND_APPOINTMENT,
        CREATE_REVIEW,
        UPDATE_PROFILE,
        CHANGE_PASSWORD,
        ADMIN_OPERATION
    }

    public enum ResultStatus {
        SUCCESS, FAILED, PENDING
    }
}
