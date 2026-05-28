package com.club.management.club.repository;

import com.club.management.club.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社团成员Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long>, JpaSpecificationExecutor<ClubMember> {

    List<ClubMember> findByClubId(Long clubId);

    List<ClubMember> findByUserId(Long userId);

    ClubMember findByClubIdAndUserId(Long clubId, Long userId);

    List<ClubMember> findByClubIdAndStatus(Long clubId, Integer status);
}
