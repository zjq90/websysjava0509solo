package com.appsys.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页查询结果类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@Schema(description = "分页查询结果")
public class PageResult<T> {

    @Schema(description = "数据列表")
    private List<T> records;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "当前页码")
    private int page;

    @Schema(description = "每页大小")
    private int size;

    @Schema(description = "总页数")
    private int totalPages;

    private PageResult() {
    }

    public PageResult(List<T> records, long total, int page, int size, int totalPages) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
    }

    /**
     * 从Spring Data Page构建结果
     */
    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages()
        );
    }

    /**
     * 手动构建结果
     */
    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        int totalPages = (int) Math.ceil((double) total / size);
        return new PageResult<>(records, total, page, size, totalPages);
    }
}
