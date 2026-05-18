package com.secondhand.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long reviewerId;

    @Column(nullable = false)
    private Long reviewedId;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(length = 1000)
    private String content;

    @Column(length = 500)
    private String images;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
