package com.medical.appointment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sms_codes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 10, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private CodeType type;

    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expireAt;

    @Column(name = "is_used")
    @Builder.Default
    private Boolean isUsed = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireAt);
    }

    public boolean isValid(String inputCode) {
        return !isUsed && !isExpired() && code.equals(inputCode);
    }

    public enum CodeType {
        LOGIN,
        REGISTER,
        VERIFY_PHONE,
        RESET_PASSWORD
    }
}
