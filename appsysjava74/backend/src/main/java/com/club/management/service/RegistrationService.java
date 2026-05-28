package com.club.management.service;

import com.club.management.common.BusinessException;
import com.club.management.common.PageQuery;
import com.club.management.dto.AuditDTO;
import com.club.management.dto.RegistrationDTO;
import com.club.management.entity.Activity;
import com.club.management.entity.Registration;
import com.club.management.entity.User;
import com.club.management.entity.enums.ActivityStatus;
import com.club.management.entity.enums.RegistrationScope;
import com.club.management.entity.enums.RegistrationStatus;
import com.club.management.repository.ActivityRepository;
import com.club.management.repository.ClubMemberRepository;
import com.club.management.repository.RegistrationRepository;
import com.club.management.repository.UserRepository;
import com.club.management.util.ExcelUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报名服务
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ClubMemberRepository clubMemberRepository;

    /**
     * 用户报名活动
     */
    @Transactional
    public Registration register(RegistrationDTO dto, Long userId) {
        log.info("用户报名活动: activityId={}, userId={}", dto.getActivityId(), userId);

        Activity activity = activityRepository.findById(dto.getActivityId())
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        if (activity.getStatus() != ActivityStatus.REGISTRATION_OPEN) {
            throw new BusinessException("活动报名未开放或已结束");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getRegistrationStartTime()) || 
            now.isAfter(activity.getRegistrationEndTime())) {
            throw new BusinessException("不在报名时间范围内");
        }

        if (registrationRepository.existsByActivityIdAndUserIdAndDeletedFalse(
                dto.getActivityId(), userId)) {
            throw new BusinessException("您已报名该活动");
        }

        if (activity.getRegistrationScope() == RegistrationScope.CLUB_MEMBERS_ONLY) {
            boolean isMember = clubMemberRepository.existsByClubIdAndUserIdAndStatusAndDeletedFalse(
                    activity.getClubId(), userId, 1);
            if (!isMember) {
                throw new BusinessException("该活动仅对社团成员开放");
            }
        }

        long approvedCount = registrationRepository.countByActivityIdAndStatusIn(
                dto.getActivityId(), List.of(RegistrationStatus.APPROVED, RegistrationStatus.PENDING));
        if (approvedCount >= activity.getQuota()) {
            throw new BusinessException("报名名额已满");
        }

        Registration registration = new Registration();
        registration.setActivityId(dto.getActivityId());
        registration.setUserId(userId);
        registration.setStudentNo(user.getStudentNo());
        registration.setRealName(user.getRealName());
        registration.setDepartment(user.getDepartment());
        registration.setMajor(user.getMajor());
        registration.setClassName(user.getClassName());
        registration.setPhone(user.getPhone());
        registration.setRemark(dto.getRemark());
        registration.setCreateBy(userId);

        if (activity.getNeedApproval()) {
            registration.setStatus(RegistrationStatus.PENDING);
        } else {
            registration.setStatus(RegistrationStatus.APPROVED);
            activity.setRegisteredCount(activity.getRegisteredCount() + 1);
            
            if (activity.getRegisteredCount() >= activity.getQuota()) {
                activity.setStatus(ActivityStatus.REGISTRATION_CLOSED);
                log.info("活动报名名额已满，自动关闭报名通道: activityId={}", dto.getActivityId());
            }
            activityRepository.save(activity);
        }

        registration = registrationRepository.save(registration);
        log.info("报名成功: registrationId={}, activityId={}, userId={}, status={}",
                registration.getId(), dto.getActivityId(), userId, registration.getStatus());

        return registration;
    }

    /**
     * 审核报名
     */
    @Transactional
    public Registration auditRegistration(AuditDTO dto, Long auditorId) {
        log.info("审核报名: registrationId={}, status={}, auditorId={}", 
                dto.getRegistrationId(), dto.getStatus(), auditorId);

        Registration registration = registrationRepository.findById(dto.getRegistrationId())
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("报名记录不存在"));

        if (registration.getStatus() != RegistrationStatus.PENDING) {
            throw new BusinessException("只有待审核的报名才能审核");
        }

        if (dto.getStatus() != RegistrationStatus.APPROVED && 
            dto.getStatus() != RegistrationStatus.REJECTED) {
            throw new BusinessException("审核状态不合法");
        }

        Activity activity = activityRepository.findById(registration.getActivityId())
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        if (dto.getStatus() == RegistrationStatus.APPROVED) {
            long approvedCount = registrationRepository.countByActivityIdAndStatusIn(
                    registration.getActivityId(), List.of(RegistrationStatus.APPROVED));
            if (approvedCount >= activity.getQuota()) {
                throw new BusinessException("报名名额已满，无法通过审核");
            }
            activity.setRegisteredCount(activity.getRegisteredCount() + 1);
            if (activity.getRegisteredCount() >= activity.getQuota()) {
                activity.setStatus(ActivityStatus.REGISTRATION_CLOSED);
            }
            activityRepository.save(activity);
        }

        registration.setStatus(dto.getStatus());
        registration.setAuditTime(LocalDateTime.now());
        registration.setAuditBy(auditorId);
        registration.setAuditRemark(dto.getRemark());
        registration.setUpdateBy(auditorId);

        registration = registrationRepository.save(registration);
        log.info("审核完成: registrationId={}, status={}", registration.getId(), registration.getStatus());

        return registration;
    }

    /**
     * 取消报名
     */
    @Transactional
    public void cancelRegistration(Long registrationId, Long userId) {
        log.info("取消报名: registrationId={}, userId={}", registrationId, userId);

        Registration registration = registrationRepository.findById(registrationId)
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("报名记录不存在"));

        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException("只能取消自己的报名");
        }

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new BusinessException("报名已取消");
        }

        Activity activity = activityRepository.findById(registration.getActivityId())
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        if (activity.getStatus() == ActivityStatus.ONGOING || 
            activity.getStatus() == ActivityStatus.COMPLETED) {
            throw new BusinessException("活动进行中或已结束，无法取消报名");
        }

        if (registration.getStatus() == RegistrationStatus.APPROVED) {
            activity.setRegisteredCount(Math.max(0, activity.getRegisteredCount() - 1));
            if (activity.getStatus() == ActivityStatus.REGISTRATION_CLOSED && 
                activity.getRegisteredCount() < activity.getQuota()) {
                activity.setStatus(ActivityStatus.REGISTRATION_OPEN);
            }
            activityRepository.save(activity);
        }

        registration.setStatus(RegistrationStatus.CANCELLED);
        registration.setUpdateBy(userId);
        registrationRepository.save(registration);

        log.info("取消报名成功: registrationId={}", registrationId);
    }

    /**
     * 分页查询活动报名列表
     */
    public Page<Registration> getRegistrationList(Long activityId, PageQuery pageQuery) {
        Pageable pageable = PageRequest.of(
                pageQuery.getPageNum() - 1,
                pageQuery.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime")
        );
        return registrationRepository.findByActivityIdAndDeletedFalse(activityId, pageable);
    }

    /**
     * 分页查询用户报名列表
     */
    public Page<Registration> getUserRegistrationList(Long userId, PageQuery pageQuery) {
        Pageable pageable = PageRequest.of(
                pageQuery.getPageNum() - 1,
                pageQuery.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime")
        );
        return registrationRepository.findByUserIdAndDeletedFalse(userId, pageable);
    }

    /**
     * 获取报名详情
     */
    public Registration getRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("报名记录不存在"));
    }

    /**
     * 导出报名名单
     */
    public byte[] exportRegistrationList(Long activityId, String department, String studentNo) {
        log.info("导出报名名单: activityId={}, department={}, studentNo={}", 
                activityId, department, studentNo);

        List<Registration> registrations = registrationRepository.findByActivityIdWithFilters(
                activityId,
                department,
                studentNo,
                List.of(RegistrationStatus.APPROVED, RegistrationStatus.PENDING, RegistrationStatus.REJECTED)
        );

        return ExcelUtil.exportRegistrationList(registrations);
    }

    /**
     * 批量审核报名
     */
    @Transactional
    public void batchAudit(List<AuditDTO> dtoList, Long auditorId) {
        log.info("批量审核报名: count={}, auditorId={}", dtoList.size(), auditorId);
        for (AuditDTO dto : dtoList) {
            auditRegistration(dto, auditorId);
        }
    }
}
