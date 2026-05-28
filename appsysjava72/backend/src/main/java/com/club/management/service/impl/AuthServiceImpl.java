package com.club.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.club.management.common.ResultCode;
import com.club.management.dto.LoginDTO;
import com.club.management.dto.RegisterDTO;
import com.club.management.entity.NotificationSetting;
import com.club.management.entity.SysUser;
import com.club.management.exception.BusinessException;
import com.club.management.mapper.NotificationSettingMapper;
import com.club.management.mapper.SysUserMapper;
import com.club.management.service.AuthService;
import com.club.management.utils.JwtUtil;
import com.club.management.utils.UserContext;
import com.club.management.vo.LoginVO;
import com.club.management.vo.SysUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private NotificationSettingMapper notificationSettingMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String VERIFY_CODE_PREFIX = "verify_code:";
    private static final long VERIFY_CODE_EXPIRE = 5;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        log.info("用户登录, 登录类型: {}", loginDTO.getLoginType());

        SysUser user = null;

        switch (loginDTO.getLoginType()) {
            case "phone":
                user = loginByPhone(loginDTO.getPhone(), loginDTO.getCode());
                break;
            case "password":
                user = loginByPassword(loginDTO.getUsername(), loginDTO.getPassword());
                break;
            case "wechat":
                user = loginByWechat(loginDTO.getOpenid());
                break;
            case "qq":
                user = loginByQQ(loginDTO.getQqOpenid());
                break;
            case "campus":
                user = loginByCampus(loginDTO.getCampusToken());
                break;
            default:
                throw new BusinessException("不支持的登录类型");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        SysUserVO userVO = new SysUserVO();
        BeanUtils.copyProperties(user, userVO);

        log.info("用户登录成功, 用户ID: {}, 用户名: {}", user.getId(), user.getUsername());

        return new LoginVO(token, userVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO register(RegisterDTO registerDTO) {
        log.info("用户注册, 手机号: {}", registerDTO.getPhone());

        String verifyCodeKey = VERIFY_CODE_PREFIX + registerDTO.getPhone();
        String storedCode = (String) redisTemplate.opsForValue().get(verifyCodeKey);

        if (storedCode == null) {
            throw new BusinessException(ResultCode.VERIFY_CODE_EXPIRED);
        }

        if (!storedCode.equals(registerDTO.getCode())) {
            throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
        }

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, registerDTO.getPhone());
        if (sysUserMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.PHONE_ALREADY_EXIST);
        }

        SysUser user = new SysUser();
        user.setPhone(registerDTO.getPhone());
        user.setUsername(registerDTO.getPhone());
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : "用户" + registerDTO.getPhone().substring(7));
        user.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + UUID.randomUUID());

        if (registerDTO.getPassword() != null && !registerDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        }

        user.setStudentNo(registerDTO.getStudentNo());
        user.setCollege(registerDTO.getCollege());
        user.setMajor(registerDTO.getMajor());
        user.setGrade(registerDTO.getGrade());
        user.setStatus(1);

        sysUserMapper.insert(user);

        NotificationSetting setting = new NotificationSetting();
        setting.setUserId(user.getId());
        setting.setActivityReminder(1);
        setting.setClubAnnouncement(1);
        setting.setNewMemberNotice(1);
        setting.setActivitySignInReminder(1);
        setting.setChatMessageNotice(1);
        notificationSettingMapper.insert(setting);

        redisTemplate.delete(verifyCodeKey);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        SysUserVO userVO = new SysUserVO();
        BeanUtils.copyProperties(user, userVO);

        log.info("用户注册成功, 用户ID: {}, 手机号: {}", user.getId(), user.getPhone());

        return new LoginVO(token, userVO);
    }

    @Override
    public void sendVerifyCode(String phone) {
        log.info("发送验证码, 手机号: {}", phone);

        String code = String.format("%06d", (int) ((Math.random() * 9 + 1) * 100000));

        String key = VERIFY_CODE_PREFIX + phone;
        redisTemplate.opsForValue().set(key, code, VERIFY_CODE_EXPIRE, TimeUnit.MINUTES);

        log.info("验证码已发送, 手机号: {}, 验证码: {}", phone, code);
    }

    @Override
    public void logout() {
        Long userId = UserContext.getUserId();
        log.info("用户退出登录, 用户ID: {}", userId);
    }

    private SysUser loginByPhone(String phone, String code) {
        if (phone == null || code == null) {
            throw new BusinessException("手机号和验证码不能为空");
        }

        String verifyCodeKey = VERIFY_CODE_PREFIX + phone;
        String storedCode = (String) redisTemplate.opsForValue().get(verifyCodeKey);

        if (storedCode == null) {
            throw new BusinessException(ResultCode.VERIFY_CODE_EXPIRED);
        }

        if (!storedCode.equals(code)) {
            throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
        }

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, phone);
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            user = new SysUser();
            user.setPhone(phone);
            user.setUsername(phone);
            user.setNickname("用户" + phone.substring(7));
            user.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + UUID.randomUUID());
            user.setStatus(1);
            sysUserMapper.insert(user);

            NotificationSetting setting = new NotificationSetting();
            setting.setUserId(user.getId());
            setting.setActivityReminder(1);
            setting.setClubAnnouncement(1);
            setting.setNewMemberNotice(1);
            setting.setActivitySignInReminder(1);
            setting.setChatMessageNotice(1);
            notificationSettingMapper.insert(setting);
        }

        redisTemplate.delete(verifyCodeKey);

        return user;
    }

    private SysUser loginByPassword(String username, String password) {
        if (username == null || password == null) {
            throw new BusinessException("用户名和密码不能为空");
        }

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        return user;
    }

    private SysUser loginByWechat(String openid) {
        if (openid == null) {
            throw new BusinessException("openid不能为空");
        }

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getOpenid, openid);
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            user = new SysUser();
            user.setOpenid(openid);
            user.setUsername("wx_" + openid.substring(0, 10));
            user.setNickname("微信用户");
            user.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + UUID.randomUUID());
            user.setStatus(1);
            sysUserMapper.insert(user);

            NotificationSetting setting = new NotificationSetting();
            setting.setUserId(user.getId());
            setting.setActivityReminder(1);
            setting.setClubAnnouncement(1);
            setting.setNewMemberNotice(1);
            setting.setActivitySignInReminder(1);
            setting.setChatMessageNotice(1);
            notificationSettingMapper.insert(setting);
        }

        return user;
    }

    private SysUser loginByQQ(String qqOpenid) {
        if (qqOpenid == null) {
            throw new BusinessException("QQ openid不能为空");
        }

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getQqOpenid, qqOpenid);
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            user = new SysUser();
            user.setQqOpenid(qqOpenid);
            user.setUsername("qq_" + qqOpenid.substring(0, 10));
            user.setNickname("QQ用户");
            user.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + UUID.randomUUID());
            user.setStatus(1);
            sysUserMapper.insert(user);

            NotificationSetting setting = new NotificationSetting();
            setting.setUserId(user.getId());
            setting.setActivityReminder(1);
            setting.setClubAnnouncement(1);
            setting.setNewMemberNotice(1);
            setting.setActivitySignInReminder(1);
            setting.setChatMessageNotice(1);
            notificationSettingMapper.insert(setting);
        }

        return user;
    }

    private SysUser loginByCampus(String campusToken) {
        if (campusToken == null) {
            throw new BusinessException("校园认证token不能为空");
        }

        Map<String, String> campusInfo = mockCampusAuth(campusToken);

        String studentNo = campusInfo.get("studentNo");

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getStudentNo, studentNo);
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            user = new SysUser();
            user.setStudentNo(studentNo);
            user.setUsername(studentNo);
            user.setNickname(campusInfo.get("name"));
            user.setCollege(campusInfo.get("college"));
            user.setMajor(campusInfo.get("major"));
            user.setGrade(campusInfo.get("grade"));
            user.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + UUID.randomUUID());
            user.setStatus(1);
            sysUserMapper.insert(user);

            NotificationSetting setting = new NotificationSetting();
            setting.setUserId(user.getId());
            setting.setActivityReminder(1);
            setting.setClubAnnouncement(1);
            setting.setNewMemberNotice(1);
            setting.setActivitySignInReminder(1);
            setting.setChatMessageNotice(1);
            notificationSettingMapper.insert(setting);
        } else {
            user.setCollege(campusInfo.get("college"));
            user.setMajor(campusInfo.get("major"));
            user.setGrade(campusInfo.get("grade"));
            sysUserMapper.updateById(user);
        }

        return user;
    }

    private Map<String, String> mockCampusAuth(String token) {
        Map<String, String> info = new HashMap<>();
        info.put("studentNo", "2021" + String.format("%04d", (int) (Math.random() * 10000)));
        info.put("name", "校园用户");
        info.put("college", "计算机学院");
        info.put("major", "软件工程");
        info.put("grade", "2021级");
        return info;
    }
}
