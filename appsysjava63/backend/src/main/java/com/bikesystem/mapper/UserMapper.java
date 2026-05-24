package com.bikesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bikesystem.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
