package com.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询参数
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@Schema(description = "分页查询参数")
public class PageQuery {

    /**
     * 页码，默认1
     */
    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    /**
     * 每页大小，默认10
     */
    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    /**
     * 搜索关键词
     */
    @Schema(description = "搜索关键词", example = "计算机")
    private String keyword;

    /**
     * 状态筛选
     */
    @Schema(description = "状态筛选", example = "NORMAL")
    private String status;

    /**
     * 类型筛选
     */
    @Schema(description = "类型筛选", example = "ACADEMIC")
    private String type;
}
