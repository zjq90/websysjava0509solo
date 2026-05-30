package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_category")
public class Category extends BaseEntity {
    private String name;
    private String icon;
    private Integer sort;
    private Integer gameCount;
}
