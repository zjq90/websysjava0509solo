package com.secondhand.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_point_record")
@EntityListeners(AuditingEntityListener.class)
public class PointRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "change_type", nullable = false)
    private String changeType;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "description")
    private String description;

    @Column(name = "related_id")
    private Long relatedId;

    @Column(name = "related_type")
    private String relatedType;

    @CreatedDate
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "deleted")
    private Boolean deleted = false;

    public static final String TYPE_SIGN = "SIGN";
    public static final String TYPE_PUBLISH = "PUBLISH";
    public static final String TYPE_ORDER = "ORDER";
    public static final String TYPE_EXCHANGE = "EXCHANGE";
    public static final String TYPE_TASK = "TASK";
    public static final String TYPE_BARGAIN = "BARGAIN";
    public static final String TYPE_GROUPBUY = "GROUPBUY";
}
