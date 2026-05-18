package com.heritage.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 捐赠实体类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "donation")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "heritage_id", nullable = false)
    private Long heritageId;

    @Column(name = "museum_id", nullable = false)
    private Long museumId;

    @Column(name = "museum_name", length = 200)
    private String museumName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String images;

    @Column(name = "donation_status")
    private Integer donationStatus = 0;

    @Column(name = "review_remark", length = 500)
    private String reviewRemark;

    @Column(name = "review_time")
    private LocalDateTime reviewTime;

    @Column(name = "certificate_no", length = 100)
    private String certificateNo;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
