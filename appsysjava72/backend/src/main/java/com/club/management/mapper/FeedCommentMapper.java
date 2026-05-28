package com.club.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.club.management.entity.FeedComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态评论Mapper接口
 *
 * @author club-management
 * @since 2024-01-01
 */
@Mapper
public interface FeedCommentMapper extends BaseMapper<FeedComment> {
}
