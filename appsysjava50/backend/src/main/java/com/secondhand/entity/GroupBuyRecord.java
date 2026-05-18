package com.secondhand.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 拼团记录实体类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_group_buy_record")
@EntityListeners(AuditingEntityListener.class)
public class GroupBuyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    @Column(name = "group_no", nullable = false, length = 50)
    private String groupNo;

    @Column(name = "leader_id", nullable = false)
    private Long leaderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "current_size")
    private Integer currentSize = 1;

    @Column(length = 20)
    private String status = "PENDING";

    @Column(name = "success_time")
    private LocalDateTime successTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Transient
    private User user;

    @Transient
    private User leader;

}
