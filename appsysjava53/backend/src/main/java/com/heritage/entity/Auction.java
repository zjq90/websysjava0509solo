package com.heritage.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 拍卖实体类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "auction")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "heritage_id", nullable = false)
    private Long heritageId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "start_price", nullable = false)
    private BigDecimal startPrice;

    @Column(name = "min_increment", nullable = false)
    private BigDecimal minIncrement;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "highest_bid_id")
    private Long highestBidId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "bid_count")
    private Integer bidCount = 0;

    @Column(name = "viewer_count")
    private Integer viewerCount = 0;

    @Column(name = "auction_status")
    private Integer auctionStatus = 1;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
