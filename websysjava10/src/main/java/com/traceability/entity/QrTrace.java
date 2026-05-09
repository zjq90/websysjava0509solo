package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 二维码追踪实体类
 * 记录二维码扫描和追踪信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "qr_trace")
public class QrTrace extends BaseEntity {

    @Column(name = "qr_code", nullable = false, length = 100)
    private String qrCode;

    @Column(name = "package_no", length = 50)
    private String packageNo;

    @Column(name = "batch_no", length = 50)
    private String batchNo;

    @Column(name = "scan_time", length = 20)
    private String scanTime;

    @Column(name = "scan_location", length = 200)
    private String scanLocation;

    @Column(name = "scanner_ip", length = 50)
    private String scannerIp;

    @Column(name = "scan_device", length = 100)
    private String scanDevice;

    @Column(name = "scan_count")
    private Integer scanCount = 0;

    @Column(name = "remark", length = 500)
    private String remark;
}
