package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    @Column(name = "transaction_no", unique = true, nullable = false, length = 50)
    private String transactionNo;

    @Column(name = "heritage_id")
    private Long heritageId;

    @Column(name = "heritage_code", length = 50)
    private String heritageCode;

    @Column(name = "heritage_name", length = 200)
    private String heritageName;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "seller_name", length = 100)
    private String sellerName;

    @Column(name = "buyer_id")
    private Long buyerId;

    @Column(name = "buyer_name", length = 100)
    private String buyerName;

    @Column(name = "buyer_phone", length = 20)
    private String buyerPhone;

    @Column(name = "buyer_email", length = 100)
    private String buyerEmail;

    @Column(name = "transaction_price", nullable = false)
    private BigDecimal transactionPrice;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    @Column(name = "transaction_type", length = 50)
    private String transactionType;

    @Column(name = "auction_house", length = 200)
    private String auctionHouse;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;
}
