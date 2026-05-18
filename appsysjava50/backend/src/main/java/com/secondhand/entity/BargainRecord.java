package com.secondhand.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 砍价记录实体类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_bargain_record")
@EntityListeners(AuditingEntityListener.class)
public class BargainRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    @Column(name = "initiator_id", nullable = false)
    private Long initiatorId;

    @Column(name = "current_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "help_count")
    private Integer helpCount = 0;

    @Column(length = 20)
    private String status = "IN_PROGRESS";

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Transient
    private User initiator;

}
