package com.appsys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "session_id", nullable = false, length = 50)
    private String sessionId;

    @Column(name = "sender_type", nullable = false, length = 20)
    private String senderType;

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "message_type", length = 20)
    private String messageType = "TEXT";

    @Column(name = "is_ai", nullable = false)
    private Boolean isAi = true;

    @Column(name = "transfer_to_human", nullable = false)
    private Boolean transferToHuman = false;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "agent_name", length = 50)
    private String agentName;
}
