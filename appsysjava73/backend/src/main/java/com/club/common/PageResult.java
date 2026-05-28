package com.club.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页结果封装类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
public class PageResult<T> {

    private List<T> list;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pageSize > 0 ? (int) Math.ceil((double) total / pageSize) : 0;
    }

    public static <T> PageResult<T> of(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }
}
