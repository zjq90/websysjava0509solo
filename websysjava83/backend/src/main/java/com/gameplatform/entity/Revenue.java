package com.gameplatform.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "revenues")
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String revenueType;

    @Column(nullable = false)
    private Double amount;

    private Long userId;

    private Long gameId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String description;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
