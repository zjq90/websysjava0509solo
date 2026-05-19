package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "data_source_trace")
public class DataSourceTrace extends BaseEntity {

    @Column(name = "trace_no", unique = true, nullable = false, length = 50)
    private String traceNo;

    @Column(name = "data_type", length = 50)
    private String dataType;

    @Column(name = "data_id")
    private Long dataId;

    @Column(name = "data_code", length = 50)
    private String dataCode;

    @Column(name = "data_name", length = 200)
    private String dataName;

    @Column(name = "source_type", length = 50)
    private String sourceType;

    @Column(name = "source_name", length = 200)
    private String sourceName;

    @Column(name = "source_url", length = 500)
    private String sourceUrl;

    @Column(name = "fetch_time", nullable = false)
    private LocalDateTime fetchTime;

    @Column(name = "fetch_method", length = 50)
    private String fetchMethod;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "operator_name", length = 100)
    private String operatorName;

    @Column(name = "data_version", length = 50)
    private String dataVersion;

    @Column(name = "data_hash", length = 100)
    private String dataHash;

    @Column(name = "parent_trace_id")
    private Long parentTraceId;

    @Column(name = "transformation_rule", columnDefinition = "TEXT")
    private String transformationRule;

    @Column(name = "quality_score")
    private Integer qualityScore;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "remark", length = 500)
    private String remark;
}
