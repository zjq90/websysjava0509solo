package com.bikesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bikesystem.entity.CreditRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 信用分记录Mapper接口
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Mapper
public interface CreditRecordMapper extends BaseMapper<CreditRecord> {
}
