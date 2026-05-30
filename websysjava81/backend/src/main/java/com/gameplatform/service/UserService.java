package com.gameplatform.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplatform.dto.BanUserDTO;
import com.gameplatform.dto.UserQueryDTO;
import com.gameplatform.entity.*;
import com.gameplatform.mapper.*;
import com.gameplatform.vo.UserDetailVO;
import com.gameplatform.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BanRecordMapper banRecordMapper;

    @Autowired
    private GameRecordMapper gameRecordMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LoginDeviceMapper loginDeviceMapper;

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    public Page<UserVO> getUserList(UserQueryDTO query) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(User::getUserId, query.getKeyword())
                    .or().like(User::getNickname, query.getKeyword())
                    .or().like(User::getPhone, query.getKeyword()));
        }
        
        if (query.getMemberStatus() != null) {
            wrapper.eq(User::getMemberStatus, query.getMemberStatus());
        }
        
        if (query.getUserStatus() != null) {
            wrapper.eq(User::getUserStatus, query.getUserStatus());
        }
        
        if (query.getStartTime() != null) {
            wrapper.ge(User::getRegisterTime, query.getStartTime());
        }
        
        if (query.getEndTime() != null) {
            wrapper.le(User::getRegisterTime, query.getEndTime());
        }
        
        wrapper.orderByDesc(User::getRegisterTime);
        
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<User> userPage = userMapper.selectPage(page, wrapper);
        
        List<UserVO> voList = userPage.getRecords().stream().map(this::convertToVO)
                .collect(Collectors.toList());
        
        Page<UserVO> result = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        result.setRecords(voList);
        return result;
    }

    public UserDetailVO getUserDetail(String userId) {
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserId, userId);
        User user = userMapper.selectOne(userWrapper);
        
        if (user == null) {
            return null;
        }
        
        UserDetailVO vo = new UserDetailVO();
        user.setPhone(maskPhone(user.getPhone()));
        vo.setUser(user);
        
        LambdaQueryWrapper<GameRecord> gameRecordWrapper = new LambdaQueryWrapper<>();
        gameRecordWrapper.eq(GameRecord::getUserId, userId)
                .orderByDesc(GameRecord::getStartTime)
                .last("LIMIT 20");
        vo.setGameRecords(gameRecordMapper.selectList(gameRecordWrapper));
        
        LambdaQueryWrapper<Favorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        vo.setFavorites(favoriteMapper.selectList(favoriteWrapper));
        
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getUserId, userId)
                .orderByDesc(Comment::getCreateTime);
        vo.setComments(commentMapper.selectList(commentWrapper));
        
        LambdaQueryWrapper<LoginDevice> deviceWrapper = new LambdaQueryWrapper<>();
        deviceWrapper.eq(LoginDevice::getUserId, userId)
                .orderByDesc(LoginDevice::getLoginTime);
        vo.setLoginDevices(loginDeviceMapper.selectList(deviceWrapper));
        
        LambdaQueryWrapper<UserBehavior> behaviorWrapper = new LambdaQueryWrapper<>();
        behaviorWrapper.eq(UserBehavior::getUserId, userId)
                .orderByAsc(UserBehavior::getCreateTime);
        vo.setBehaviors(userBehaviorMapper.selectList(behaviorWrapper));
        
        LambdaQueryWrapper<BanRecord> banWrapper = new LambdaQueryWrapper<>();
        banWrapper.eq(BanRecord::getUserId, userId)
                .orderByDesc(BanRecord::getCreateTime);
        vo.setBanRecords(banRecordMapper.selectList(banWrapper));
        
        return vo;
    }

    @Transactional
    public void banUser(BanUserDTO dto) {
        BanRecord record = new BanRecord();
        record.setUserId(dto.getUserId());
        record.setBanType(dto.getBanType());
        record.setBanReason(dto.getBanReason());
        record.setUnbanTime(dto.getUnbanTime());
        record.setOperatorId(dto.getOperatorId());
        record.setOperatorName(dto.getOperatorName());
        record.setStatus(1);
        record.setCreateTime(LocalDateTime.now());
        banRecordMapper.insert(record);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, dto.getUserId());
        User user = new User();
        user.setUserStatus(1);
        userMapper.update(user, wrapper);
    }

    @Transactional
    public void unbanUser(Long banRecordId) {
        BanRecord record = banRecordMapper.selectById(banRecordId);
        if (record != null) {
            record.setStatus(0);
            banRecordMapper.updateById(record);
            
            LambdaQueryWrapper<BanRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BanRecord::getUserId, record.getUserId())
                    .eq(BanRecord::getStatus, 1);
            Long count = banRecordMapper.selectCount(wrapper);
            if (count == 0) {
                LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
                userWrapper.eq(User::getUserId, record.getUserId());
                User user = new User();
                user.setUserStatus(0);
                userMapper.update(user, userWrapper);
            }
        }
    }

    @Transactional
    public void batchBanUsers(List<BanUserDTO> list) {
        for (BanUserDTO dto : list) {
            banUser(dto);
        }
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        vo.setUserId(user.getUserId());
        vo.setNickname(user.getNickname());
        vo.setPhone(maskPhone(user.getPhone()));
        vo.setMemberStatus(user.getMemberStatus());
        vo.setUserStatus(user.getUserStatus());
        vo.setTotalGameTime(user.getTotalGameTime());
        vo.setRegisterTime(user.getRegisterTime());
        vo.setLastLoginTime(user.getLastLoginTime());
        return vo;
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    public List<BanRecord> getBanRecords(String userId) {
        LambdaQueryWrapper<BanRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BanRecord::getUserId, userId)
                .orderByDesc(BanRecord::getCreateTime);
        return banRecordMapper.selectList(wrapper);
    }
}
