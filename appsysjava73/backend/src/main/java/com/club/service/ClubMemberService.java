package com.club.service;

import com.club.common.PageResult;
import com.club.entity.*;
import com.club.entity.enums.MemberRoleEnum;
import com.club.exception.BusinessException;
import com.club.repository.ActivityParticipantRepository;
import com.club.repository.ClubDepartmentRepository;
import com.club.repository.ClubMemberRepository;
import com.club.repository.ClubRepository;
import com.club.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ClubMemberService {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubDepartmentRepository clubDepartmentRepository;

    @Autowired
    private ActivityParticipantRepository activityParticipantRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    public ClubMember addMember(Long clubId, Long userId, Long departmentId, String role, Long operatorId) {
        log.info("添加社团成员 - clubId: {}, userId: {}, departmentId: {}, role: {}, operatorId: {}", clubId, userId, departmentId, role, operatorId);

        checkClubPermission(clubId, operatorId);

        if (clubMemberRepository.existsByClubIdAndUserIdAndDeletedFalse(clubId, userId)) {
            log.warn("用户已是社团成员 - clubId: {}, userId: {}", clubId, userId);
            return clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(clubId, userId).orElse(null);
        }

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        ClubDepartment department = null;
        String departmentName = null;
        if (departmentId != null) {
            department = clubDepartmentRepository.findByIdAndClubIdAndDeletedFalse(departmentId, clubId).orElse(null);
            if (department != null) {
                departmentName = department.getName();
            }
        }

        ClubMember member = new ClubMember();
        member.setClubId(clubId);
        member.setUserId(userId);
        member.setUsername(user.getUsername());
        member.setRealName(user.getRealName());
        member.setAvatar(user.getAvatar());
        member.setStudentNo(user.getStudentNo());
        member.setPhone(user.getPhone());
        member.setCollege(user.getCollege());
        member.setMajor(user.getMajor());
        member.setGrade(user.getGrade());
        member.setDepartmentId(departmentId);
        member.setDepartmentName(departmentName);
        member.setRole(role != null ? MemberRoleEnum.valueOf(role) : MemberRoleEnum.NORMAL);
        member.setActive(1);
        member.setActivityCount(0);
        member.setJoinTime(LocalDateTime.now().format(FORMATTER));
        member.setBio(user.getBio());

        ClubMember saved = clubMemberRepository.save(member);

        clubRepository.incrementMemberCount(clubId);

        if (departmentId != null && department != null) {
            department.setMemberCount(department.getMemberCount() + 1);
            clubDepartmentRepository.save(department);
        }

        log.info("社团成员添加成功 - memberId: {}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public ClubMember addMember(Long clubId, Long userId, Long departmentId, String departmentName) {
        log.info("添加社团成员 - clubId: {}, userId: {}, departmentId: {}", clubId, userId, departmentId);

        if (clubMemberRepository.existsByClubIdAndUserIdAndDeletedFalse(clubId, userId)) {
            log.warn("用户已是社团成员 - clubId: {}, userId: {}", clubId, userId);
            return clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(clubId, userId).orElse(null);
        }

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        ClubMember member = new ClubMember();
        member.setClubId(clubId);
        member.setUserId(userId);
        member.setUsername(user.getUsername());
        member.setRealName(user.getRealName());
        member.setAvatar(user.getAvatar());
        member.setStudentNo(user.getStudentNo());
        member.setPhone(user.getPhone());
        member.setCollege(user.getCollege());
        member.setMajor(user.getMajor());
        member.setGrade(user.getGrade());
        member.setDepartmentId(departmentId);
        member.setDepartmentName(departmentName);
        member.setRole(MemberRoleEnum.NORMAL);
        member.setActive(1);
        member.setActivityCount(0);
        member.setJoinTime(LocalDateTime.now().format(FORMATTER));
        member.setBio(user.getBio());

        ClubMember saved = clubMemberRepository.save(member);

        clubRepository.incrementMemberCount(clubId);

        if (departmentId != null) {
            ClubDepartment department = clubDepartmentRepository.findByIdAndClubIdAndDeletedFalse(departmentId, clubId).orElse(null);
            if (department != null) {
                department.setMemberCount(department.getMemberCount() + 1);
                clubDepartmentRepository.save(department);
            }
        }

        log.info("社团成员添加成功 - memberId: {}", saved.getId());
        return saved;
    }

    public PageResult<ClubMember> getMemberList(Integer pageNum, Integer pageSize, Long clubId, Long departmentId, String role) {
        log.info("获取社团成员列表 - clubId: {}, departmentId: {}, pageNum: {}, pageSize: {}, role: {}", clubId, departmentId, pageNum, pageSize, role);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubMember> page;

        if (departmentId != null) {
            page = clubMemberRepository.findByClubIdAndDepartmentIdAndDeletedFalseOrderByRoleDesc(clubId, departmentId, pageable);
        } else {
            page = clubMemberRepository.findByClubIdAndDeletedFalseOrderByRoleDesc(clubId, pageable);
        }

        return PageResult.of(page);
    }

    public List<ClubMember> getAllMembers(Long clubId) {
        log.info("获取所有社团成员 - clubId: {}", clubId);
        return clubMemberRepository.findByClubIdAndDeletedFalse(clubId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ClubMember updateMember(ClubMember member, Long operatorId) {
        Long clubId = member.getClubId();
        Long memberId = member.getId();
        log.info("更新成员信息 - clubId: {}, memberId: {}, operatorId: {}", clubId, memberId, operatorId);

        checkClubPermission(clubId, operatorId);

        ClubMember existMember = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("成员不存在"));

        if (!existMember.getClubId().equals(clubId)) {
            throw new BusinessException("成员不属于该社团");
        }

        if (member.getDepartmentId() != null) {
            if (existMember.getDepartmentId() != null && !existMember.getDepartmentId().equals(member.getDepartmentId())) {
                ClubDepartment oldDept = clubDepartmentRepository.findByIdAndClubIdAndDeletedFalse(existMember.getDepartmentId(), clubId).orElse(null);
                if (oldDept != null) {
                    oldDept.setMemberCount(Math.max(0, oldDept.getMemberCount() - 1));
                    clubDepartmentRepository.save(oldDept);
                }
                ClubDepartment newDept = clubDepartmentRepository.findByIdAndClubIdAndDeletedFalse(member.getDepartmentId(), clubId).orElse(null);
                if (newDept != null) {
                    newDept.setMemberCount(newDept.getMemberCount() + 1);
                    clubDepartmentRepository.save(newDept);
                }
            }
            existMember.setDepartmentId(member.getDepartmentId());
            existMember.setDepartmentName(member.getDepartmentName());
        }

        if (member.getRole() != null) {
            if (member.getRole() == MemberRoleEnum.PRESIDENT) {
                ClubMember currentPresident = clubMemberRepository.findByClubIdAndRoleAndDeletedFalse(clubId, MemberRoleEnum.PRESIDENT).orElse(null);
                if (currentPresident != null && !currentPresident.getId().equals(memberId)) {
                    currentPresident.setRole(MemberRoleEnum.NORMAL);
                    clubMemberRepository.save(currentPresident);
                }
                Club club = clubRepository.findByIdAndDeletedFalse(clubId).orElse(null);
                if (club != null) {
                    club.setLeaderId(existMember.getUserId());
                    club.setLeaderName(existMember.getRealName());
                    clubRepository.save(club);
                }
            }
            existMember.setRole(member.getRole());
        }

        if (member.getActive() != null) {
            existMember.setActive(member.getActive());
        }

        if (member.getBio() != null) {
            existMember.setBio(member.getBio());
        }

        ClubMember saved = clubMemberRepository.save(existMember);
        log.info("成员信息更新成功 - memberId: {}", memberId);
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeMember(Long clubId, Long memberId, Long operatorId) {
        log.info("移除社团成员 - clubId: {}, memberId: {}, operatorId: {}", clubId, memberId, operatorId);

        checkClubPermission(clubId, operatorId);

        ClubMember member = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("成员不存在"));

        if (!member.getClubId().equals(clubId)) {
            throw new BusinessException("成员不属于该社团");
        }

        if (member.getRole() == MemberRoleEnum.PRESIDENT) {
            throw new BusinessException("不能移除社长，请先转让社长权限");
        }

        member.setDeleted(true);
        clubMemberRepository.save(member);

        clubRepository.decrementMemberCount(clubId);

        if (member.getDepartmentId() != null) {
            ClubDepartment department = clubDepartmentRepository.findByIdAndClubIdAndDeletedFalse(member.getDepartmentId(), clubId).orElse(null);
            if (department != null) {
                department.setMemberCount(Math.max(0, department.getMemberCount() - 1));
                clubDepartmentRepository.save(department);
            }
        }

        log.info("社团成员移除成功 - memberId: {}", memberId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void transferPresident(Long clubId, Long newMemberId, Long operatorId) {
        log.info("转让社长权限 - clubId: {}, newMemberId: {}, operatorId: {}", clubId, newMemberId, operatorId);

        Club club = clubRepository.findByIdAndDeletedFalse(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));

        if (!club.getLeaderId().equals(operatorId)) {
            throw new BusinessException("只有社长才能转让权限");
        }

        ClubMember oldPresident = clubMemberRepository.findByClubIdAndRoleAndDeletedFalse(clubId, MemberRoleEnum.PRESIDENT)
                .orElseThrow(() -> new BusinessException("社长不存在"));

        ClubMember newPresident = clubMemberRepository.findById(newMemberId)
                .orElseThrow(() -> new BusinessException("新社长不存在"));

        if (!newPresident.getClubId().equals(clubId)) {
            throw new BusinessException("新社长不属于该社团");
        }

        oldPresident.setRole(MemberRoleEnum.NORMAL);
        clubMemberRepository.save(oldPresident);

        newPresident.setRole(MemberRoleEnum.PRESIDENT);
        clubMemberRepository.save(newPresident);

        club.setLeaderId(newPresident.getUserId());
        club.setLeaderName(newPresident.getRealName());
        clubRepository.save(club);

        log.info("社长权限转让成功 - oldUserId: {}, newUserId: {}", oldPresident.getUserId(), newPresident.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void quitClub(Long clubId, Long userId) {
        log.info("成员退出社团 - clubId: {}, userId: {}", clubId, userId);

        ClubMember member = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(clubId, userId)
                .orElseThrow(() -> new BusinessException("你不是该社团成员"));

        if (member.getRole() == MemberRoleEnum.PRESIDENT) {
            throw new BusinessException("社长不能直接退出，请先转让社长权限");
        }

        member.setDeleted(true);
        clubMemberRepository.save(member);

        clubRepository.decrementMemberCount(clubId);

        if (member.getDepartmentId() != null) {
            ClubDepartment department = clubDepartmentRepository.findByIdAndClubIdAndDeletedFalse(member.getDepartmentId(), clubId).orElse(null);
            if (department != null) {
                department.setMemberCount(Math.max(0, department.getMemberCount() - 1));
                clubDepartmentRepository.save(department);
            }
        }

        log.info("成员退出社团成功 - userId: {}", userId);
    }

    public Map<String, Object> getMemberActivityRecords(Long memberId) {
        log.info("获取成员活动参与记录 - memberId: {}", memberId);

        ClubMember member = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("成员不存在"));

        List<ActivityParticipant> records = activityParticipantRepository.findByClubIdAndUserIdAndDeletedFalse(member.getClubId(), member.getUserId());

        Map<String, Object> result = new HashMap<>();
        result.put("member", member);
        result.put("activityRecords", records);
        result.put("totalActivities", records.size());

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void markMemberActive(Long memberId, Integer active, Long operatorId) {
        log.info("标记成员活跃状态 - memberId: {}, active: {}, operatorId: {}", memberId, active, operatorId);

        ClubMember member = clubMemberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException("成员不存在"));

        checkClubPermission(member.getClubId(), operatorId);

        member.setActive(active);
        clubMemberRepository.save(member);

        log.info("成员活跃状态更新成功 - memberId: {}, active: {}", memberId, active);
    }

    public String exportMemberList(Long clubId, Long userId) {
        log.info("导出成员名单 - clubId: {}, userId: {}", clubId, userId);

        checkClubPermission(clubId, userId);

        List<ClubMember> members = clubMemberRepository.findByClubIdAndDeletedFalse(clubId);

        StringBuilder sb = new StringBuilder();
        sb.append("序号,姓名,学号,院系,专业,手机号,部门,角色,加入时间\n");
        for (int i = 0; i < members.size(); i++) {
            ClubMember m = members.get(i);
            sb.append(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    i + 1,
                    m.getRealName() != null ? m.getRealName() : "",
                    m.getStudentNo() != null ? m.getStudentNo() : "",
                    m.getCollege() != null ? m.getCollege() : "",
                    m.getMajor() != null ? m.getMajor() : "",
                    m.getPhone() != null ? m.getPhone() : "",
                    m.getDepartmentName() != null ? m.getDepartmentName() : "",
                    m.getRole() != null ? m.getRole().getDesc() : "",
                    m.getJoinTime() != null ? m.getJoinTime() : ""
            ));
        }

        log.info("导出成员名单成功 - 共{}条", members.size());
        return sb.toString();
    }

    public List<ClubMember> getMyClubs(Long userId) {
        log.info("获取我加入的社团列表 - userId: {}", userId);
        return clubMemberRepository.findByUserIdAndDeletedFalse(userId);
    }

    private void checkClubPermission(Long clubId, Long userId) {
        ClubMember operator = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(clubId, userId)
                .orElseThrow(() -> new BusinessException("你不是该社团成员"));

        if (operator.getRole() != MemberRoleEnum.PRESIDENT && operator.getRole() != MemberRoleEnum.VICE_PRESIDENT) {
            throw new BusinessException("权限不足，只有社长或副社长可以执行此操作");
        }
    }
}
