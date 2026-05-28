package com.club.service;

import com.club.entity.Club;
import com.club.entity.ClubDepartment;
import com.club.entity.ClubMember;
import com.club.entity.enums.MemberRoleEnum;
import com.club.exception.BusinessException;
import com.club.repository.ClubDepartmentRepository;
import com.club.repository.ClubMemberRepository;
import com.club.repository.ClubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 社团部门服务
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
public class ClubDepartmentService {

    @Autowired
    private ClubDepartmentRepository clubDepartmentRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private ClubRepository clubRepository;

    /**
     * 获取社团部门列表
     */
    public List<ClubDepartment> getDepartmentList(Long clubId) {
        log.info("获取社团部门列表 - clubId: {}", clubId);
        return clubDepartmentRepository.findByClubIdAndDeletedFalseOrderBySortOrderAsc(clubId);
    }

    /**
     * 创建部门
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubDepartment createDepartment(Long clubId, ClubDepartment department, Long userId) {
        log.info("创建社团部门 - clubId: {}, name: {}, userId: {}", clubId, department.getName(), userId);

        checkClubPermission(clubId, userId);

        if (clubDepartmentRepository.existsByNameAndClubIdAndDeletedFalse(department.getName(), clubId)) {
            throw new BusinessException("部门名称已存在");
        }

        department.setClubId(clubId);
        department.setMemberCount(0);
        if (department.getSortOrder() == null) {
            department.setSortOrder(1);
        }

        ClubDepartment saved = clubDepartmentRepository.save(department);
        log.info("部门创建成功 - departmentId: {}", saved.getId());
        return saved;
    }

    /**
     * 更新部门信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubDepartment updateDepartment(Long clubId, Long departmentId, ClubDepartment department, Long userId) {
        log.info("更新部门信息 - clubId: {}, departmentId: {}, userId: {}", clubId, departmentId, userId);

        checkClubPermission(clubId, userId);

        ClubDepartment existDept = clubDepartmentRepository.findByIdAndClubIdAndDeletedFalse(departmentId, clubId)
                .orElseThrow(() -> new BusinessException("部门不存在"));

        if (department.getName() != null && !department.getName().equals(existDept.getName())) {
            if (clubDepartmentRepository.existsByNameAndClubIdAndDeletedFalse(department.getName(), clubId)) {
                throw new BusinessException("部门名称已存在");
            }
            existDept.setName(department.getName());
        }

        if (department.getDescription() != null) {
            existDept.setDescription(department.getDescription());
        }
        if (department.getLeaderId() != null) {
            existDept.setLeaderId(department.getLeaderId());
        }
        if (department.getLeaderName() != null) {
            existDept.setLeaderName(department.getLeaderName());
        }
        if (department.getSortOrder() != null) {
            existDept.setSortOrder(department.getSortOrder());
        }

        ClubDepartment saved = clubDepartmentRepository.save(existDept);
        log.info("部门信息更新成功 - departmentId: {}", departmentId);
        return saved;
    }

    /**
     * 删除部门
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(Long clubId, Long departmentId, Long userId) {
        log.info("删除部门 - clubId: {}, departmentId: {}, userId: {}", clubId, departmentId, userId);

        checkClubPermission(clubId, userId);

        ClubDepartment department = clubDepartmentRepository.findByIdAndClubIdAndDeletedFalse(departmentId, clubId)
                .orElseThrow(() -> new BusinessException("部门不存在"));

        long memberCount = clubMemberRepository.countByClubIdAndDepartmentIdAndDeletedFalse(clubId, departmentId);
        if (memberCount > 0) {
            throw new BusinessException("部门下还有成员，无法删除");
        }

        department.setDeleted(true);
        clubDepartmentRepository.save(department);

        log.info("部门删除成功 - departmentId: {}", departmentId);
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
