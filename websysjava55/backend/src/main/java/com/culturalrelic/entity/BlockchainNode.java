package com.culturalrelic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 区块链节点实体类
 * 支持高可用部署，跨可用区配置
 */
@Data
@Entity
@Table(name = "blockchain_node")
@EqualsAndHashCode(callSuper = true)
public class BlockchainNode extends BaseEntity {

    /**
     * 节点名称
     */
    @Column(name = "node_name", nullable = false, length = 100)
    private String nodeName;

    /**
     * 节点地址（IP或域名）
     */
    @Column(name = "node_address", nullable = false, length = 200)
    private String nodeAddress;

    /**
     * 端口号
     */
    @Column(name = "port", nullable = false)
    private Integer port;

    /**
     * 节点类型：1-主节点，2-备份节点，3-同步节点
     */
    @Column(name = "node_type", nullable = false)
    private Integer nodeType;

    /**
     * 可用区标识（用于跨可用区部署）
     */
    @Column(name = "availability_zone", length = 100)
    private String availabilityZone;

    /**
     * 区域/数据中心
     */
    @Column(name = "region", length = 100)
    private String region;

    /**
     * 节点状态：0-离线，1-在线，2-同步中，3-维护中
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 连接协议：http, https, ws, wss
     */
    @Column(name = "protocol", length = 20)
    private String protocol = "http";

    /**
     * API密钥
     */
    @Column(name = "api_key", length = 500)
    private String apiKey;

    /**
     * 节点版本
     */
    @Column(name = "version", length = 50)
    private String version;

    /**
     * 区块高度
     */
    @Column(name = "block_height")
    private Long blockHeight;

    /**
     * 最后同步时间
     */
    @Column(name = "last_sync_time")
    private String lastSyncTime;

    /**
     * 优先级（用于主节点选举）
     */
    @Column(name = "priority")
    private Integer priority = 5;

    /**
     * 是否启用：0-禁用，1-启用
     */
    @Column(name = "enabled", nullable = false)
    private Integer enabled = 1;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
