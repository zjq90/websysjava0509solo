package com.club.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.club.management.entity.ClubMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 社团成员Mapper接口
 *
 * @author club-management
 * @since 2024-01-01
 */
@Mapper
public interface ClubMemberMapper extends BaseMapper<ClubMember> {
}
