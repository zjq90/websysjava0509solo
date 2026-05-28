package com.club.repository;

import com.club.entity.ClubHonor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团荣誉Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubHonorRepository extends JpaRepository<ClubHonor, Long>, JpaSpecificationExecutor<ClubHonor> {

    List<ClubHonor> findByClubIdAndDeletedFalseOrderBySortOrderAsc(Long clubId);

    Optional<ClubHonor> findByIdAndDeletedFalse(Long id);
}
