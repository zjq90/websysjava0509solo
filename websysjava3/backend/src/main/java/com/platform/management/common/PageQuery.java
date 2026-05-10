package com.platform.management.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询参数
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@Schema(description = "分页查询参数")
public class PageQuery {

    @Schema(description = "页码，默认1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数，默认10")
    private Integer pageSize = 10;

    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "排序方式：asc/desc")
    private String orderType = "desc";
}
