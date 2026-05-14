package com.appsys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "check_in_record")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "签到记录实体")
public class CheckInRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "签到日期")
    private LocalDate checkInDate;

    @Column(nullable = false)
    @Schema(description = "获得积分")
    private Integer points;

    @Column(nullable = false)
    @Schema(description = "连续签到天数")
    private Integer continuousDays;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
