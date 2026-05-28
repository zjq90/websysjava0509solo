package com.club.repository;

import com.club.entity.ClubDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团部门Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubDepartmentRepository extends JpaRepository<ClubDepartment, Long>, JpaSpecificationExecutor<ClubDepartment> {

    List<ClubDepartment> findByClubIdAndDeletedFalseOrderBySortOrderAsc(Long clubId);

    Optional<ClubDepartment> findByIdAndClubIdAndDeletedFalse(Long id, Long clubId);

    Optional<ClubDepartment> findByNameAndClubIdAndDeletedFalse(String name, Long clubId);

    boolean existsByNameAndClubIdAndDeletedFalse(String name, Long clubId);
}
