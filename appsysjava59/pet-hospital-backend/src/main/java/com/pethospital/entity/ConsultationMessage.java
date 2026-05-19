package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "consultation_message")
public class ConsultationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consultation_id", nullable = false)
    private Long consultationId;

    @Column(name = "sender_type", length = 20)
    private String senderType;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(length = 2000)
    private String content;

    @Column(name = "message_type", length = 20)
    private String messageType;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (isRead == null) {
            isRead = false;
        }
    }
}
