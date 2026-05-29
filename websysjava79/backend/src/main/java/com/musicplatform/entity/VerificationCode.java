package com.musicplatform.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "verification_codes")
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String target;

    @Enumerated(EnumType.STRING)
    private CodeType type;

    @Column(nullable = false)
    private String code;

    private int expireMinutes = 5;

    private boolean used = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(createdAt.plusMinutes(expireMinutes));
    }

    public enum CodeType {
        EMAIL_REGISTER, PHONE_REGISTER, PASSWORD_RESET, LOGIN
    }
}
