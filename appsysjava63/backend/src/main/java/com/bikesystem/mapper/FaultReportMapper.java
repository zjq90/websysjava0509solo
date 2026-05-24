package com.bikesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bikesystem.entity.FaultReport;
import org.apache.ibatis.annotations.Mapper;

/**
 * 故障上报Mapper接口
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Mapper
public interface FaultReportMapper extends BaseMapper<FaultReport> {
}
