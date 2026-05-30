package com.gamesys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamesys.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
