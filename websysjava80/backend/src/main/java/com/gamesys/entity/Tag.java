package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_tag")
public class Tag extends BaseEntity {
    private String name;
    private String alias;
    private Integer gameCount;
}
