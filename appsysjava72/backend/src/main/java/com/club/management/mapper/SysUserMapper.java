package com.club.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.club.management.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author club-management
 * @since 2024-01-01
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
