package com.secondhand.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 自提点实体类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_pickup_point")
@EntityListeners(AuditingEntityListener.class)
public class PickupPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String businessHours;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String status = "ACTIVE";

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Transient
    private Double distance;

}
