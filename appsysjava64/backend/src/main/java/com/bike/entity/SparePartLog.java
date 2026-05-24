package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 备件出入库记录实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "spare_part_log")
public class SparePartLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long partId;

    @Column(length = 50)
    private String partCode;

    @Column(length = 100)
    private String partName;

    @Column(nullable = false, length = 20)
    private String operationType;

    @Column(nullable = false)
    private Integer quantity;

    @Column
    private Integer beforeQuantity;

    @Column
    private Integer afterQuantity;

    @Column(length = 500)
    private String remark;

    @Column
    private Long operatorId;

    @Column(length = 50)
    private String operatorName;

    @Column
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
