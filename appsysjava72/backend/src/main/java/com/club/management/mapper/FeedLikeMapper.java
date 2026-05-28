package com.club.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.club.management.entity.FeedLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态点赞Mapper接口
 *
 * @author club-management
 * @since 2024-01-01
 */
@Mapper
public interface FeedLikeMapper extends BaseMapper<FeedLike> {
}
