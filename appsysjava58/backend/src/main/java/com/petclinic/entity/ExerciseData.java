package com.petclinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 运动监测数据实体类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "exercise_data")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ExerciseData extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    @Column(name = "steps")
    private Integer steps;

    @Column(name = "distance", precision = 8, scale = 2)
    private java.math.BigDecimal distance;

    @Column(name = "calories_burned", precision = 8, scale = 2)
    private java.math.BigDecimal caloriesBurned;

    @Column(name = "active_minutes")
    private Integer activeMinutes;

    @Column(name = "resting_minutes")
    private Integer restingMinutes;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "device_id", length = 100)
    private String deviceId;

    @Column(name = "sync_time")
    private LocalDateTime syncTime;

    @Column(name = "data_source", length = 50)
    private String dataSource;
}