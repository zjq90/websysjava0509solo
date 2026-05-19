package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "data_subscription")
public class DataSubscription extends BaseEntity {

    @Column(name = "subscription_no", unique = true, nullable = false, length = 50)
    private String subscriptionNo;

    @Column(name = "subscriber_id", nullable = false)
    private Long subscriberId;

    @Column(name = "subscriber_name", length = 100)
    private String subscriberName;

    @Column(name = "topic", nullable = false, length = 100)
    private String topic;

    @Column(name = "topic_desc", length = 500)
    private String topicDesc;

    @Column(name = "filter_condition", columnDefinition = "TEXT")
    private String filterCondition;

    @Column(name = "notify_type", length = 50)
    private String notifyType;

    @Column(name = "notify_target", length = 200)
    private String notifyTarget;

    @Column(name = "frequency", length = 50)
    private String frequency;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "last_push_time")
    private LocalDateTime lastPushTime;

    @Column(name = "push_count", nullable = false)
    private Integer pushCount = 0;

    @Column(name = "is_data_masking", nullable = false)
    private Boolean isDataMasking = true;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "remark", length = 500)
    private String remark;
}
