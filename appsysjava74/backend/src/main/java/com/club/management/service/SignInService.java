package com.club.management.service;

import com.club.management.common.BusinessException;
import com.club.management.dto.SignInDTO;
import com.club.management.entity.Activity;
import com.club.management.entity.Registration;
import com.club.management.entity.SignIn;
import com.club.management.entity.User;
import com.club.management.entity.enums.ActivityStatus;
import com.club.management.entity.enums.RegistrationStatus;
import com.club.management.entity.enums.SignInStatus;
import com.club.management.repository.ActivityRepository;
import com.club.management.repository.RegistrationRepository;
import com.club.management.repository.SignInRepository;
import com.club.management.repository.UserRepository;
import com.club.management.util.ExcelUtil;
import com.club.management.vo.SignInStatisticsVO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 签到服务
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {

    private final SignInRepository signInRepository;
    private final ActivityRepository activityRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SIGN_IN_TOKEN_KEY = "sign_in:token:";

    /**
     * 用户扫码签到
     */
    @Transactional
    public SignIn signIn(SignInDTO dto, Long userId, HttpServletRequest request) {
        log.info("用户签到: activityId={}, userId={}", dto.getActivityId(), userId);

        String cacheKey = SIGN_IN_TOKEN_KEY + dto.getActivityId() + ":" + dto.getQrToken();
        Object cachedToken = redisTemplate.opsForValue().get(cacheKey);
        
        Activity activity = activityRepository.findById(dto.getActivityId())
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        if (activity.getStatus() != ActivityStatus.ONGOING && 
            activity.getStatus() != ActivityStatus.REGISTRATION_CLOSED) {
            throw new BusinessException("活动未开始或已结束");
        }

        if (activity.getQrCodeToken() == null || 
            !activity.getQrCodeToken().equals(dto.getQrToken())) {
            throw new BusinessException("二维码无效");
        }

        if (activity.getQrCodeExpireTime() != null && 
            activity.getQrCodeExpireTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("二维码已过期，请刷新");
        }

        LocalDateTime now = LocalDateTime.now();
        if (activity.getSignInStartTime() != null && now.isBefore(activity.getSignInStartTime())) {
            throw new BusinessException("签到尚未开始");
        }
        if (activity.getSignInEndTime() != null && now.isAfter(activity.getSignInEndTime())) {
            throw new BusinessException("签到已结束");
        }

        Registration registration = registrationRepository.findByActivityIdAndUserId(dto.getActivityId(), userId)
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new BusinessException("您未报名该活动"));

        if (registration.getStatus() != RegistrationStatus.APPROVED) {
            throw new BusinessException("您的报名尚未通过审核");
        }

        if (signInRepository.existsByActivityIdAndUserIdAndStatusInAndDeletedFalse(
                dto.getActivityId(), userId, 
                List.of(SignInStatus.SIGNED, SignInStatus.LATE, SignInStatus.MAKE_UP))) {
            throw new BusinessException("您已签到，请勿重复签到");
        }

        String deviceKey = "sign_in:device:" + dto.getActivityId() + ":" + dto.getDeviceInfo();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(deviceKey))) {
            throw new BusinessException("检测到代签行为，请使用自己的设备签到");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        SignIn signIn = signInRepository.findByActivityIdAndUserIdAndDeletedFalse(
                dto.getActivityId(), userId).orElse(new SignIn());
        signIn.setActivityId(dto.getActivityId());
        signIn.setRegistrationId(registration.getId());
        signIn.setUserId(userId);
        signIn.setStudentNo(user.getStudentNo());
        signIn.setRealName(user.getRealName());
        signIn.setDepartment(user.getDepartment());
        signIn.setSignInTime(now);
        signIn.setSignInIp(getClientIp(request));
        signIn.setSignInDevice(dto.getDeviceInfo());
        signIn.setSignInLocation(dto.getLocation());
        signIn.setSignInMethod("QR_CODE");
        signIn.setQrToken(dto.getQrToken());
        signIn.setCreateBy(userId);

        boolean isLate = activity.getSignInStartTime() != null && 
                         now.isAfter(activity.getSignInStartTime().plusMinutes(10));
        signIn.setStatus(isLate ? SignInStatus.LATE : SignInStatus.SIGNED);

        signIn = signInRepository.save(signIn);

        registration.setSignedIn(true);
        registration.setSignInTime(now);
        registrationRepository.save(registration);

        redisTemplate.opsForValue().set(deviceKey, userId, 12, TimeUnit.HOURS);
        updateActivityAttendance(activity);

        log.info("签到成功: signInId={}, activityId={}, userId={}, status={}",
                signIn.getId(), dto.getActivityId(), userId, signIn.getStatus());

        return signIn;
    }

    /**
     * 手动补签
     */
    @Transactional
    public SignIn makeUpSignIn(Long activityId, Long userId, String reason, Long operatorId) {
        log.info("手动补签: activityId={}, userId={}, operatorId={}", activityId, userId, operatorId);

        Activity activity = activityRepository.findById(activityId)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        Registration registration = registrationRepository.findByActivityIdAndUserId(activityId, userId)
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new BusinessException("该用户未报名此活动"));

        if (registration.getStatus() != RegistrationStatus.APPROVED) {
            throw new BusinessException("该用户报名尚未通过审核");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        SignIn signIn = signInRepository.findByActivityIdAndUserIdAndDeletedFalse(
                activityId, userId).orElse(new SignIn());
        signIn.setActivityId(activityId);
        signIn.setRegistrationId(registration.getId());
        signIn.setUserId(userId);
        signIn.setStudentNo(user.getStudentNo());
        signIn.setRealName(user.getRealName());
        signIn.setDepartment(user.getDepartment());
        signIn.setSignInTime(LocalDateTime.now());
        signIn.setSignInMethod("MANUAL");
        signIn.setMakeUpReason(reason);
        signIn.setMakeUpBy(operatorId);
        signIn.setMakeUpTime(LocalDateTime.now());
        signIn.setStatus(SignInStatus.MAKE_UP);
        signIn.setUpdateBy(operatorId);
        if (signIn.getCreateBy() == null) {
            signIn.setCreateBy(operatorId);
        }

        signIn = signInRepository.save(signIn);

        registration.setSignedIn(true);
        registration.setSignInTime(LocalDateTime.now());
        registrationRepository.save(registration);

        updateActivityAttendance(activity);

        log.info("补签成功: signInId={}, activityId={}, userId={}", 
                signIn.getId(), activityId, userId);

        return signIn;
    }

    /**
     * 获取活动签到列表
     */
    public List<SignIn> getSignInList(Long activityId) {
        return signInRepository.findByActivityIdAndDeletedFalse(activityId);
    }

    /**
     * 获取签到统计
     */
    public SignInStatisticsVO getSignInStatistics(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        List<SignIn> signInList = signInRepository.findByActivityIdAndDeletedFalse(activityId);
        
        long approvedCount = registrationRepository.countByActivityIdAndStatusIn(
                activityId, List.of(RegistrationStatus.APPROVED));

        Map<SignInStatus, Long> statusCount = signInList.stream()
                .collect(Collectors.groupingBy(SignIn::getStatus, Collectors.counting()));

        long signedCount = statusCount.getOrDefault(SignInStatus.SIGNED, 0L) +
                          statusCount.getOrDefault(SignInStatus.MAKE_UP, 0L) +
                          statusCount.getOrDefault(SignInStatus.LATE, 0L);
        long notSignedCount = approvedCount - signedCount;

        SignInStatisticsVO vo = new SignInStatisticsVO();
        vo.setActivityId(activityId);
        vo.setActivityName(activity.getName());
        vo.setRegistrationCount((int) approvedCount);
        vo.setAttendedCount((int) signedCount);
        vo.setAttendanceRate(approvedCount > 0 ? (signedCount * 100.0 / approvedCount) : 0);
        vo.setSignedCount(statusCount.getOrDefault(SignInStatus.SIGNED, 0L));
        vo.setNotSignedCount(notSignedCount);
        vo.setLateCount(statusCount.getOrDefault(SignInStatus.LATE, 0L));
        vo.setMakeUpCount(statusCount.getOrDefault(SignInStatus.MAKE_UP, 0L));
        vo.setAbsentCount(statusCount.getOrDefault(SignInStatus.ABSENT, 0L));

        return vo;
    }

    /**
     * 导出签到表
     */
    public byte[] exportSignInList(Long activityId, String department, String studentNo) {
        log.info("导出签到表: activityId={}, department={}, studentNo={}", 
                activityId, department, studentNo);

        List<SignIn> signInList = signInRepository.findByActivityIdWithFilters(
                activityId, department, studentNo);
        return ExcelUtil.exportSignInList(signInList);
    }

    /**
     * 批量标记缺席
     */
    @Transactional
    public void markAbsent(Long activityId, Long operatorId) {
        log.info("批量标记缺席: activityId={}, operatorId={}", activityId, operatorId);

        List<Registration> registrations = registrationRepository.findByActivityIdAndStatusAndDeletedFalse(
                activityId, RegistrationStatus.APPROVED);

        for (Registration registration : registrations) {
            boolean hasSigned = signInRepository.existsByActivityIdAndUserIdAndStatusInAndDeletedFalse(
                    activityId, registration.getUserId(),
                    List.of(SignInStatus.SIGNED, SignInStatus.LATE, SignInStatus.MAKE_UP));

            if (!hasSigned) {
                User user = userRepository.findById(registration.getUserId())
                        .orElse(null);
                if (user != null) {
                    SignIn signIn = new SignIn();
                    signIn.setActivityId(activityId);
                    signIn.setRegistrationId(registration.getId());
                    signIn.setUserId(registration.getUserId());
                    signIn.setStudentNo(user.getStudentNo());
                    signIn.setRealName(user.getRealName());
                    signIn.setDepartment(user.getDepartment());
                    signIn.setStatus(SignInStatus.ABSENT);
                    signIn.setCreateBy(operatorId);
                    signInRepository.save(signIn);
                }
            }
        }

        Activity activity = activityRepository.findById(activityId).orElseThrow();
        updateActivityAttendance(activity);
        log.info("批量标记缺席完成: activityId={}", activityId);
    }

    private void updateActivityAttendance(Activity activity) {
        SignInStatisticsVO stats = getSignInStatistics(activity.getId());
        activity.setAttendedCount(stats.getAttendedCount());
        activity.setAttendanceRate(stats.getAttendanceRate());
        activityRepository.save(activity);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
