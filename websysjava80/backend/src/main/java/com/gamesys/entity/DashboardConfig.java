package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dashboard_config")
public class DashboardConfig extends BaseEntity {
    private String name;
    private String description;
    private String layoutConfig;
    private Integer isDefault;
}
