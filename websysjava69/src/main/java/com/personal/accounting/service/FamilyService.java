package com.personal.accounting.service;

import com.personal.accounting.entity.Family;
import com.personal.accounting.entity.FamilyMember;
import com.personal.accounting.entity.User;
import com.personal.accounting.enums.FamilyRole;
import com.personal.accounting.repository.FamilyMemberRepository;
import com.personal.accounting.repository.FamilyRepository;
import com.personal.accounting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final UserRepository userRepository;

    public List<Family> getFamiliesByUserId(Long userId) {
        List<FamilyMember> members = familyMemberRepository.findByUserId(userId);
        return members.stream()
                .map(m -> familyRepository.findById(m.getFamilyId()).orElse(null))
                .filter(f -> f != null)
                .collect(Collectors.toList());
    }

    public Family getFamilyById(Long id) {
        return familyRepository.findById(id).orElseThrow(() -> new RuntimeException("家庭不存在"));
    }

    @Transactional
    public Family createFamily(Family family, Long creatorId) {
        family.setCreatedBy(creatorId);
        Family savedFamily = familyRepository.save(family);

        FamilyMember adminMember = new FamilyMember();
        adminMember.setFamilyId(savedFamily.getId());
        adminMember.setUserId(creatorId);
        adminMember.setRole(FamilyRole.ADMIN);
        familyMemberRepository.save(adminMember);

        return savedFamily;
    }

    @Transactional
    public Family updateFamily(Long id, Family family) {
        Family existing = getFamilyById(id);
        existing.setFamilyName(family.getFamilyName());
        existing.setDescription(family.getDescription());
        return familyRepository.save(existing);
    }

    @Transactional
    public void deleteFamily(Long id) {
        List<FamilyMember> members = familyMemberRepository.findByFamilyId(id);
        familyMemberRepository.deleteAll(members);
        familyRepository.deleteById(id);
    }

    public List<FamilyMember> getFamilyMembers(Long familyId) {
        return familyMemberRepository.findByFamilyId(familyId);
    }

    public List<Long> getFamilyMemberIds(Long familyId) {
        return familyMemberRepository.findUserIdsByFamilyId(familyId);
    }

    @Transactional
    public FamilyMember addMember(Long familyId, Long userId, FamilyRole role, Long operatorId) {
        checkAdminPermission(familyId, operatorId);

        if (familyMemberRepository.existsByFamilyIdAndUserId(familyId, userId)) {
            throw new RuntimeException("该用户已是家庭成员");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));

        FamilyMember member = new FamilyMember();
        member.setFamilyId(familyId);
        member.setUserId(userId);
        member.setRole(role);
        return familyMemberRepository.save(member);
    }

    @Transactional
    public void removeMember(Long familyId, Long userId, Long operatorId) {
        checkAdminPermission(familyId, operatorId);

        if (userId.equals(operatorId)) {
            throw new RuntimeException("不能移除自己");
        }

        FamilyMember member = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId)
                .orElseThrow(() -> new RuntimeException("该成员不存在"));
        familyMemberRepository.delete(member);
    }

    @Transactional
    public FamilyMember updateMemberRole(Long familyId, Long userId, FamilyRole role, Long operatorId) {
        checkAdminPermission(familyId, operatorId);

        FamilyMember member = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId)
                .orElseThrow(() -> new RuntimeException("该成员不存在"));
        member.setRole(role);
        return familyMemberRepository.save(member);
    }

    public boolean isFamilyMember(Long familyId, Long userId) {
        return familyMemberRepository.existsByFamilyIdAndUserId(familyId, userId);
    }

    public boolean isFamilyAdmin(Long familyId, Long userId) {
        return familyMemberRepository.findByFamilyIdAndUserId(familyId, userId)
                .map(m -> m.getRole() == FamilyRole.ADMIN)
                .orElse(false);
    }

    private void checkAdminPermission(Long familyId, Long userId) {
        if (!isFamilyAdmin(familyId, userId)) {
            throw new RuntimeException("无权限执行此操作，需要管理员权限");
        }
    }

    public boolean canViewAllRecords(Long familyId, Long userId) {
        return isFamilyAdmin(familyId, userId);
    }
}
