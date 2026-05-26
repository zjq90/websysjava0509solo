package com.personal.accounting.repository;

import com.personal.accounting.entity.FamilyMember;
import com.personal.accounting.enums.FamilyRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    List<FamilyMember> findByFamilyId(Long familyId);
    List<FamilyMember> findByUserId(Long userId);
    Optional<FamilyMember> findByFamilyIdAndUserId(Long familyId, Long userId);
    boolean existsByFamilyIdAndUserId(Long familyId, Long userId);
    
    @Query("SELECT fm.userId FROM FamilyMember fm WHERE fm.familyId = :familyId")
    List<Long> findUserIdsByFamilyId(Long familyId);
    
    List<FamilyMember> findByFamilyIdAndRole(Long familyId, FamilyRole role);
}
