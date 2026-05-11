package com.hospital.service;

import com.hospital.dto.LoginDTO;
import com.hospital.dto.LoginUserVO;
import com.hospital.dto.RegisterDTO;
import com.hospital.entity.NotificationSetting;
import com.hospital.entity.User;
import com.hospital.repository.NotificationSettingRepository;
import com.hospital.repository.UserRepository;
import com.hospital.util.JwtUtil;
import com.hospital.util.Sm4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务类
 * 处理用户注册、登录、信息管理等业务
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationSettingRepository notificationSettingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Sm4Util sm4Util;

    /**
     * 用户注册
     * 
     * @param dto 注册请求DTO
     * @return 注册结果
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginUserVO register(RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getRealName());
        user.setRole(dto.getRole() != null ? dto.getRole() : "PATIENT");
        user.setStatus(1);
        
        if (dto.getPhone() != null) {
            user.setPhone(sm4Util.encrypt(dto.getPhone()));
        }
        if (dto.getIdCard() != null) {
            user.setIdCard(sm4Util.encrypt(dto.getIdCard()));
        }
        user.setGender(dto.getGender());

        user = userRepository.save(user);

        NotificationSetting setting = new NotificationSetting();
        setting.setUserId(user.getId());
        notificationSettingRepository.save(setting);

        return buildLoginUserVO(user);
    }

    /**
     * 用户登录
     * 
     * @param dto 登录请求DTO
     * @return 登录用户信息
     */
    public LoginUserVO login(LoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        return buildLoginUserVO(user);
    }

    /**
     * 根据ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息（脱敏）
     */
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (user.getPhone() != null) {
            try {
                String phone = sm4Util.decrypt(user.getPhone());
                user.setPhone(Sm4Util.maskPhone(phone));
            } catch (Exception e) {
                user.setPhone(null);
            }
        }
        if (user.getIdCard() != null) {
            try {
                String idCard = sm4Util.decrypt(user.getIdCard());
                user.setIdCard(Sm4Util.maskIdCard(idCard));
            } catch (Exception e) {
                user.setIdCard(null);
            }
        }
        user.setPassword(null);
        
        return user;
    }

    /**
     * 获取用户通知设置
     * 
     * @param userId 用户ID
     * @return 通知设置
     */
    public NotificationSetting getNotificationSetting(Long userId) {
        return notificationSettingRepository.findByUserId(userId)
                .orElseGet(() -> {
                    NotificationSetting setting = new NotificationSetting();
                    setting.setUserId(userId);
                    return notificationSettingRepository.save(setting);
                });
    }

    /**
     * 更新用户通知设置
     * 
     * @param userId 用户ID
     * @param setting 通知设置
     * @return 更新后的设置
     */
    @Transactional(rollbackFor = Exception.class)
    public NotificationSetting updateNotificationSetting(Long userId, NotificationSetting setting) {
        NotificationSetting exist = notificationSettingRepository.findByUserId(userId)
                .orElseGet(() -> {
                    NotificationSetting ns = new NotificationSetting();
                    ns.setUserId(userId);
                    return ns;
                });

        if (setting.getAppointmentSuccess() != null) {
            exist.setAppointmentSuccess(setting.getAppointmentSuccess());
        }
        if (setting.getAppointmentReminder() != null) {
            exist.setAppointmentReminder(setting.getAppointmentReminder());
        }
        if (setting.getDoctorCancel() != null) {
            exist.setDoctorCancel(setting.getDoctorCancel());
        }
        if (setting.getReportReady() != null) {
            exist.setReportReady(setting.getReportReady());
        }
        if (setting.getSystemNotice() != null) {
            exist.setSystemNotice(setting.getSystemNotice());
        }
        if (setting.getPaymentNotice() != null) {
            exist.setPaymentNotice(setting.getPaymentNotice());
        }
        if (setting.getReminderMinutes() != null) {
            exist.setReminderMinutes(setting.getReminderMinutes());
        }
        if (setting.getPushChannel() != null) {
            exist.setPushChannel(setting.getPushChannel());
        }
        if (setting.getVisitReminder() != null) {
            exist.setVisitReminder(setting.getVisitReminder());
        }
        if (setting.getDoNotDisturb() != null) {
            exist.setDoNotDisturb(setting.getDoNotDisturb());
        }
        if (setting.getDndStart() != null) {
            exist.setDndStart(setting.getDndStart());
        }
        if (setting.getDndEnd() != null) {
            exist.setDndEnd(setting.getDndEnd());
        }

        return notificationSettingRepository.save(exist);
    }

    /**
     * 构建登录用户VO
     */
    private LoginUserVO buildLoginUserVO(User user) {
        LoginUserVO vo = new LoginUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setName(user.getName());
        vo.setRealName(user.getName());
        vo.setRole(user.getRole());
        vo.setAvatar(user.getAvatar());

        if (user.getPhone() != null) {
            try {
                vo.setPhone(Sm4Util.maskPhone(sm4Util.decrypt(user.getPhone())));
            } catch (Exception e) {
                vo.setPhone(null);
            }
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        vo.setToken(token);
        vo.setExpiresIn(jwtUtil.getExpiration() / 1000);

        return vo;
    }
}
