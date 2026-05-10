package com.websys.common;

import lombok.Data;
import java.util.List;

/**
 * 分页响应结果封装类
 * 
 * @author websys
 * @version 1.0.0
 */
@Data
public class PageResult<T> {
    /**
     * 数据列表
     */
    private List<T> records;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页码
     */
    private Integer current;
    
    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 构建分页结果
     * 
     * @param records 数据列表
     * @param total 总记录数
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long total, Integer current, Integer size) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(records);
        result.setTotal(total);
        result.setCurrent(current);
        result.setSize(size);
        return result;
    }
}
