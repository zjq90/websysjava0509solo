package com.club.repository;

import com.club.entity.ClubMember;
import com.club.entity.enums.MemberRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团成员Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long>, JpaSpecificationExecutor<ClubMember> {

    Page<ClubMember> findByClubIdAndDeletedFalseOrderByRoleDesc(Long clubId, Pageable pageable);

    Page<ClubMember> findByClubIdAndDepartmentIdAndDeletedFalseOrderByRoleDesc(Long clubId, Long departmentId, Pageable pageable);

    List<ClubMember> findByClubIdAndDeletedFalse(Long clubId);

    List<ClubMember> findByUserIdAndDeletedFalse(Long userId);

    List<ClubMember> findByClubIdAndRoleAndDeletedFalse(Long clubId, MemberRoleEnum role);

    Optional<ClubMember> findByClubIdAndUserIdAndDeletedFalse(Long clubId, Long userId);


    boolean existsByClubIdAndUserIdAndDeletedFalse(Long clubId, Long userId);

    long countByClubIdAndDeletedFalse(Long clubId);

    long countByClubIdAndDepartmentIdAndDeletedFalse(Long clubId, Long departmentId);

    long countByClubIdAndActiveAndDeletedFalse(Long clubId, Integer active);
}
