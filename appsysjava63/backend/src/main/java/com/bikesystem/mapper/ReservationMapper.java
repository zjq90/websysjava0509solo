package com.bikesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bikesystem.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 预约记录Mapper接口
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {

    /**
     * 查询用户有效预约
     */
    @Select("SELECT * FROM reservation WHERE user_id = #{userId} AND status = 'ACTIVE' AND deleted = 0 ORDER BY id DESC LIMIT 1")
    Reservation selectActiveReservation(@Param("userId") Long userId);
}
