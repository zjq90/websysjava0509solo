package com.secondhand.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user_auth")
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 50)
    private String realName;

    @Column(length = 18)
    private String idCard;

    @Column(length = 255)
    private String idCardFront;

    @Column(length = 255)
    private String idCardBack;

    @Column(length = 255)
    private String faceImage;

    @Column(nullable = false)
    private Integer status = 0;

    @Column(length = 500)
    private String remark;

    private Integer currentStep = 1;

    private LocalDateTime verifyTime;

    @Column(updatable = false)
    private LocalDateTime createTime;

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
}
