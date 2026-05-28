package com.club.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.ResultCode;
import com.club.management.entity.SysUser;
import com.club.management.exception.BusinessException;
import com.club.management.mapper.SysUserMapper;
import com.club.management.service.SysUserService;
import com.club.management.utils.UserContext;
import com.club.management.vo.SysUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public SysUserVO getCurrentUser() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        SysUserVO userVO = new SysUserVO();
        BeanUtils.copyProperties(user, userVO);

        log.debug("获取当前用户信息, 用户ID: {}", userId);

        return userVO;
    }

    @Override
    public SysUserVO getUserById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        SysUserVO userVO = new SysUserVO();
        BeanUtils.copyProperties(user, userVO);

        log.debug("获取用户信息, 用户ID: {}", id);

        return userVO;
    }

    @Override
    public SysUserVO updateUser(SysUser user) {
        Long userId = UserContext.getUserId();
        user.setId(userId);

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }

        sysUserMapper.updateById(user);

        SysUser updatedUser = sysUserMapper.selectById(userId);
        SysUserVO userVO = new SysUserVO();
        BeanUtils.copyProperties(updatedUser, userVO);

        log.info("更新用户信息成功, 用户ID: {}", userId);

        return userVO;
    }

    @Override
    public PageResult<SysUserVO> getUserPage(Page<SysUser> page, String keyword) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysUser::getNickname, keyword)
                    .or().like(SysUser::getUsername, keyword)
                    .or().like(SysUser::getPhone, keyword)
                    .or().like(SysUser::getStudentNo, keyword);
        }
        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> userPage = sysUserMapper.selectPage(page, wrapper);

        List<SysUserVO> userVOList = userPage.getRecords().stream()
                .map(user -> {
                    SysUserVO vo = new SysUserVO();
                    BeanUtils.copyProperties(user, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return new PageResult<>(userPage.getTotal(), userPage.getCurrent(), userPage.getSize(), userVOList);
    }

    @Override
    public SysUser getUserByPhone(String phone) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, phone);
        return sysUserMapper.selectOne(wrapper);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return sysUserMapper.selectOne(wrapper);
    }

    @Override
    public SysUser getUserByOpenid(String openid) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getOpenid, openid);
        return sysUserMapper.selectOne(wrapper);
    }

    @Override
    public SysUser getUserByQqOpenid(String qqOpenid) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getQqOpenid, qqOpenid);
        return sysUserMapper.selectOne(wrapper);
    }
}
