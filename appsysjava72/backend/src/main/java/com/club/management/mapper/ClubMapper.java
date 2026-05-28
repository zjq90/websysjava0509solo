package com.club.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.club.management.entity.Club;
import org.apache.ibatis.annotations.Mapper;

/**
 * 社团Mapper接口
 *
 * @author club-management
 * @since 2024-01-01
 */
@Mapper
public interface ClubMapper extends BaseMapper<Club> {
}
