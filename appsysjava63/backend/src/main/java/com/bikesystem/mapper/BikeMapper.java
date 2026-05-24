package com.bikesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bikesystem.entity.Bike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 车辆Mapper接口
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Mapper
public interface BikeMapper extends BaseMapper<Bike> {

    /**
     * 查询附近可用车辆
     */
    @Select("SELECT * FROM bike WHERE status = 'AVAILABLE' " +
            "AND latitude BETWEEN #{minLat} AND #{maxLat} " +
            "AND longitude BETWEEN #{minLng} AND #{maxLng} " +
            "ORDER BY (POWER(latitude - #{lat}, 2) + POWER(longitude - #{lng}, 2)) ASC " +
            "LIMIT #{limit}")
    List<Bike> findNearbyAvailableBikes(@Param("lat") BigDecimal lat, 
                                         @Param("lng") BigDecimal lng,
                                         @Param("minLat") BigDecimal minLat,
                                         @Param("maxLat") BigDecimal maxLat,
                                         @Param("minLng") BigDecimal minLng,
                                         @Param("maxLng") BigDecimal maxLng,
                                         @Param("limit") Integer limit);
}
