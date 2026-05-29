package com.music.platform.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "copyrights")
public class Copyright {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "music_id")
    private Long musicId;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 20)
    private String type;

    @Column(length = 50)
    private String declaration;

    @Column(name = "is_original")
    private Boolean isOriginal = true;

    @Column(name = "original_cert", length = 500)
    private String originalCert;

    @Column(name = "report_count")
    private Integer reportCount = 0;

    @Column(name = "status", length = 20)
    private String status = "active";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (reportCount == null) reportCount = 0;
    }
}
