package com.vending.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 售货机实体类
 */
@Data
@Entity
@Table(name = "vending_machine")
@Schema(description = "售货机设备")
public class VendingMachine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "设备ID")
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "设备编号", required = true)
    private String machineCode;
    
    @Column(nullable = false)
    @Schema(description = "设备名称", required = true)
    private String name;
    
    @Schema(description = "安装位置")
    private String location;
    
    @Schema(description = "IP地址")
    private String ipAddress;
    
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Schema(description = "货道总数")
    private Integer slotCount = 20;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'OFFLINE'")
    @Schema(description = "设备状态：ONLINE在线, OFFLINE离线, MAINTENANCE维护中")
    private String status = "OFFLINE";
    
    @Schema(description = "设备描述")
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "最后在线时间")
    private LocalDateTime lastOnlineTime;
    
    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Slot> slots = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
