package com.club.management.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询参数
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "分页查询参数")
public class PageQuery {

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10", defaultValue = "10")
    private Integer pageSize = 10;

    @Schema(description = "排序字段", example = "createTime")
    private String sortBy;

    @Schema(description = "排序方式", example = "desc", allowableValues = {"asc", "desc"})
    private String sortOrder = "desc";

    @Schema(description = "搜索关键词")
    private String keyword;

    public int getPageNum() {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    public int getPageSize() {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }
        return Math.min(pageSize, 100);
    }
}
