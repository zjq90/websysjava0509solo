package com.club.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.entity.SysUser;
import com.club.management.vo.SysUserVO;

/**
 * 用户服务接口
 *
 * @author club-management
 * @since 2024-01-01
 */
public interface SysUserService {

    /**
     * 获取当前登录用户信息
     */
    SysUserVO getCurrentUser();

    /**
     * 根据ID获取用户信息
     */
    SysUserVO getUserById(Long id);

    /**
     * 更新用户信息
     */
    SysUserVO updateUser(SysUser user);

    /**
     * 分页查询用户列表
     */
    PageResult<SysUserVO> getUserPage(Page<SysUser> page, String keyword);

    /**
     * 根据手机号获取用户
     */
    SysUser getUserByPhone(String phone);

    /**
     * 根据用户名获取用户
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据openid获取用户
     */
    SysUser getUserByOpenid(String openid);

    /**
     * 根据QQ openid获取用户
     */
    SysUser getUserByQqOpenid(String qqOpenid);
}
