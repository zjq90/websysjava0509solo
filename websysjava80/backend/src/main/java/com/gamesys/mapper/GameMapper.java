package com.gamesys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamesys.entity.Game;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameMapper extends BaseMapper<Game> {
}
