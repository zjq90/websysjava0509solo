package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 宠物日常数据实体类
 * 用于记录从智能设备采集的宠物日常数据，如进食量、饮水频率等
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "pet_daily_data")
@Schema(description = "宠物日常数据")
public class PetDailyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "数据ID")
    private Long id;

    @Schema(description = "设备ID")
    private Long deviceId;

    @Column(length = 100)
    @Schema(description = "设备名称")
    private String deviceName;

    @Column(length = 100)
    @Schema(description = "宠物名称")
    private String petName;

    @Column(precision = 10, scale = 2)
    @Schema(description = "进食量（克）")
    private BigDecimal foodIntake;

    @Column(precision = 10, scale = 2)
    @Schema(description = "饮水量（毫升）")
    private BigDecimal waterIntake;

    @Schema(description = "进食次数")
    private Integer feedCount;

    @Schema(description = "饮水次数")
    private Integer drinkCount;

    @Column(precision = 5, scale = 2)
    @Schema(description = "环境温度（摄氏度）")
    private BigDecimal temperature;

    @Column(precision = 5, scale = 2)
    @Schema(description = "环境湿度（百分比）")
    private BigDecimal humidity;

    @Column(length = 500)
    @Schema(description = "备注信息")
    private String remarks;

    @Column(length = 20)
    @Schema(description = "数据状态：NORMAL-正常, ABNORMAL-异常")
    private String status;

    @Schema(description = "数据记录时间")
    private LocalDateTime recordTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (recordTime == null) {
            recordTime = LocalDateTime.now();
        }
        if (status == null) {
            status = "NORMAL";
        }
    }
}
