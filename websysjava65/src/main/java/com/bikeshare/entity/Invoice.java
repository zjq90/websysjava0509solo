package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 发票实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String invoiceNo;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String companyName;

    @Column(length = 50)
    private String taxNo;

    @Column(length = 200)
    private String companyAddress;

    @Column(length = 20)
    private String companyPhone;

    @Column(length = 100)
    private String bankName;

    @Column(length = 30)
    private String bankAccount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal invoiceAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 20)
    private String invoiceType;

    @Column(nullable = false, length = 20)
    private String status = "ISSUED";

    @Column(length = 200)
    private String remark;

    private LocalDateTime issueTime;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
}
