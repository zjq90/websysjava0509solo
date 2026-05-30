package com.gamesys.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameExcelVO {

    @ExcelProperty("游戏ID")
    private String gameId;

    @ExcelProperty("游戏名称")
    private String name;

    @ExcelProperty("类型")
    private String type;

    @ExcelProperty("开发者")
    private String developer;

    @ExcelProperty("状态")
    private String status;

    @ExcelProperty("热度值")
    private Integer hotValue;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
