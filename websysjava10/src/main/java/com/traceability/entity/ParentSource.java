package com.traceability.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 亲本来源实体类
 * 记录种子的亲本来源信息，是追溯链条的第一个环节
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "parent_source")
public class ParentSource extends BaseEntity {

    @Column(name = "batch_no", nullable = false, length = 50)
    private String batchNo;

    @Column(name = "parent_name", nullable = false, length = 100)
    private String parentName;

    @Column(name = "source_origin", length = 200)
    private String sourceOrigin;

    @Column(name = "source_type", length = 50)
    private String sourceType;

    @Column(name = "collection_date", length = 20)
    private String collectionDate;

    @Column(name = "collector", length = 50)
    private String collector;

    @Column(name = "collection_location", length = 200)
    private String collectionLocation;

    @Column(name = "genetic_info", length = 500)
    private String geneticInfo;

    @Column(name = "certificate_no", length = 50)
    private String certificateNo;

    @Column(name = "remark", length = 500)
    private String remark;
}
