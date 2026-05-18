package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 溯源记录实体类（区块链存证）
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "trace_record")
public class TraceRecord extends BaseEntity {

    /**
     * 关联文物ID
     */
    @Column(name = "heritage_id", nullable = false)
    private Long heritageId;

    /**
     * 区块链交易哈希
     */
    @Column(name = "blockchain_hash", length = 100)
    private String blockchainHash;

    /**
     * 记录类型：1-出土记录，2-收藏记录，3-拍卖记录，4-修复记录，5-鉴定记录
     */
    @Column(name = "record_type", nullable = false)
    private Integer recordType;

    /**
     * 记录标题
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 记录详情
     */
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    /**
     * 发生时间
     */
    @Column(name = "occur_time")
    private LocalDateTime occurTime;

    /**
     * 地点（出土/拍卖/收藏地点）
     */
    @Column(name = "location", length = 200)
    private String location;

    /**
     * 交易价格（拍卖记录）
     */
    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    /**
     * 交易方/收藏方名称
     */
    @Column(name = "party_name", length = 200)
    private String partyName;

    /**
     * 关联凭证图片URL
     */
    @Column(name = "evidence_url", length = 500)
    private String evidenceUrl;

    /**
     * 国家文物数据库对接状态：0-未对接，1-已对接
     */
    @Column(name = "national_sync_status", nullable = false)
    private Integer nationalSyncStatus = 0;

    /**
     * 国家文物数据库记录编号
     */
    @Column(name = "national_record_no", length = 100)
    private String nationalRecordNo;
}