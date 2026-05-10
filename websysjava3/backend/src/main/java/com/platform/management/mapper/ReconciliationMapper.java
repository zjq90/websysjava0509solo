package com.platform.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.management.entity.Reconciliation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对账记录Mapper接口
 * 
 * @author platform
 * @version 1.0.0
 */
@Mapper
public interface ReconciliationMapper extends BaseMapper<Reconciliation> {
}
