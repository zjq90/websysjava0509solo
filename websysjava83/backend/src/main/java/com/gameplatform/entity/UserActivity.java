package com.gameplatform.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_activities")
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String activityType;

    @Column(nullable = false)
    private LocalDateTime activityTime;

    private String ipAddress;

    private String userAgent;
}
