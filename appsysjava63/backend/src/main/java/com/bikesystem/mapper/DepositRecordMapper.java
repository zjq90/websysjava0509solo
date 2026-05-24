package com.bikesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bikesystem.entity.DepositRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 押金记录Mapper接口
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Mapper
public interface DepositRecordMapper extends BaseMapper<DepositRecord> {
}
