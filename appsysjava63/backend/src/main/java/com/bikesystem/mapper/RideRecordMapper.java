package com.bikesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bikesystem.entity.RideRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 骑行记录Mapper接口
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Mapper
public interface RideRecordMapper extends BaseMapper<RideRecord> {

    /**
     * 查询用户进行中的骑行
     */
    @Select("SELECT * FROM ride_record WHERE user_id = #{userId} AND status = 'ONGOING' AND deleted = 0 ORDER BY id DESC LIMIT 1")
    RideRecord selectOngoingRide(@Param("userId") Long userId);
}
