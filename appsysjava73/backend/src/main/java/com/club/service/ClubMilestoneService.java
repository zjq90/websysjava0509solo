package com.club.service;

import com.club.entity.ClubMember;
import com.club.entity.ClubMilestone;
import com.club.entity.PastPresident;
import com.club.entity.enums.MemberRoleEnum;
import com.club.exception.BusinessException;
import com.club.repository.ClubMemberRepository;
import com.club.repository.ClubMilestoneRepository;
import com.club.repository.PastPresidentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 社团大事记服务
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
public class ClubMilestoneService {

    @Autowired
    private ClubMilestoneRepository clubMilestoneRepository;

    @Autowired
    private PastPresidentRepository pastPresidentRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    /**
     * 获取大事记列表
     */
    public List<ClubMilestone> getMilestoneList(Long clubId) {
        log.info("获取大事记列表 - clubId: {}", clubId);
        return clubMilestoneRepository.findByClubIdAndDeletedFalseOrderByEventDateDesc(clubId);
    }

    /**
     * 添加大事记
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubMilestone createMilestone(Long clubId, ClubMilestone milestone, Long userId) {
        log.info("添加大事记 - clubId: {}, title: {}, userId: {}", clubId, milestone.getTitle(), userId);

        checkClubPermission(clubId, userId);

        milestone.setClubId(clubId);
        ClubMilestone saved = clubMilestoneRepository.save(milestone);
        log.info("大事记添加成功 - milestoneId: {}", saved.getId());
        return saved;
    }

    /**
     * 更新大事记
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubMilestone updateMilestone(Long clubId, Long milestoneId, ClubMilestone milestone, Long userId) {
        log.info("更新大事记 - clubId: {}, milestoneId: {}, userId: {}", clubId, milestoneId, userId);

        checkClubPermission(clubId, userId);

        ClubMilestone existMilestone = clubMilestoneRepository.findByIdAndClubIdAndDeletedFalse(milestoneId, clubId)
                .orElseThrow(() -> new BusinessException("大事记不存在"));

        if (milestone.getTitle() != null) existMilestone.setTitle(milestone.getTitle());
        if (milestone.getDescription() != null) existMilestone.setDescription(milestone.getDescription());
        if (milestone.getEventDate() != null) existMilestone.setEventDate(milestone.getEventDate());
        if (milestone.getType() != null) existMilestone.setType(milestone.getType());
        if (milestone.getImage() != null) existMilestone.setImage(milestone.getImage());

        ClubMilestone saved = clubMilestoneRepository.save(existMilestone);
        log.info("大事记更新成功 - milestoneId: {}", milestoneId);
        return saved;
    }

    /**
     * 删除大事记
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMilestone(Long clubId, Long milestoneId, Long userId) {
        log.info("删除大事记 - clubId: {}, milestoneId: {}, userId: {}", clubId, milestoneId, userId);

        checkClubPermission(clubId, userId);

        ClubMilestone milestone = clubMilestoneRepository.findByIdAndClubIdAndDeletedFalse(milestoneId, clubId)
                .orElseThrow(() -> new BusinessException("大事记不存在"));

        milestone.setDeleted(true);
        clubMilestoneRepository.save(milestone);

        log.info("大事记删除成功 - milestoneId: {}", milestoneId);
    }

    /**
     * 获取历任社长列表
     */
    public List<PastPresident> getPastPresidentList(Long clubId) {
        log.info("获取历任社长列表 - clubId: {}", clubId);
        return pastPresidentRepository.findByClubIdAndDeletedFalseOrderByTermStartDesc(clubId);
    }

    /**
     * 添加历任社长
     */
    @Transactional(rollbackFor = Exception.class)
    public PastPresident createPastPresident(Long clubId, PastPresident president, Long userId) {
        log.info("添加历任社长 - clubId: {}, name: {}, userId: {}", clubId, president.getName(), userId);

        checkClubPermission(clubId, userId);

        president.setClubId(clubId);
        PastPresident saved = pastPresidentRepository.save(president);
        log.info("历任社长添加成功 - presidentId: {}", saved.getId());
        return saved;
    }

    /**
     * 更新历任社长
     */
    @Transactional(rollbackFor = Exception.class)
    public PastPresident updatePastPresident(Long clubId, Long presidentId, PastPresident president, Long userId) {
        log.info("更新历任社长 - clubId: {}, presidentId: {}, userId: {}", clubId, presidentId, userId);

        checkClubPermission(clubId, userId);

        PastPresident existPresident = pastPresidentRepository.findByIdAndClubIdAndDeletedFalse(presidentId, clubId)
                .orElseThrow(() -> new BusinessException("社长信息不存在"));

        if (president.getName() != null) existPresident.setName(president.getName());
        if (president.getAvatar() != null) existPresident.setAvatar(president.getAvatar());
        if (president.getStudentNo() != null) existPresident.setStudentNo(president.getStudentNo());
        if (president.getTermStart() != null) existPresident.setTermStart(president.getTermStart());
        if (president.getTermEnd() != null) existPresident.setTermEnd(president.getTermEnd());
        if (president.getDescription() != null) existPresident.setDescription(president.getDescription());

        PastPresident saved = pastPresidentRepository.save(existPresident);
        log.info("历任社长更新成功 - presidentId: {}", presidentId);
        return saved;
    }

    /**
     * 删除历任社长
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePastPresident(Long clubId, Long presidentId, Long userId) {
        log.info("删除历任社长 - clubId: {}, presidentId: {}, userId: {}", clubId, presidentId, userId);

        checkClubPermission(clubId, userId);

        PastPresident president = pastPresidentRepository.findByIdAndClubIdAndDeletedFalse(presidentId, clubId)
                .orElseThrow(() -> new BusinessException("社长信息不存在"));

        president.setDeleted(true);
        pastPresidentRepository.save(president);

        log.info("历任社长删除成功 - presidentId: {}", presidentId);
    }

    /**
     * 检查用户是否有社团管理权限
     */
    private void checkClubPermission(Long clubId, Long userId) {
        ClubMember operator = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(clubId, userId)
                .orElseThrow(() -> new BusinessException("你不是该社团成员"));

        if (operator.getRole() != MemberRoleEnum.PRESIDENT && operator.getRole() != MemberRoleEnum.VICE_PRESIDENT) {
            throw new BusinessException("权限不足，只有社长或副社长可以执行此操作");
        }
    }
}
