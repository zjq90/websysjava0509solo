package com.club.repository;

import com.club.entity.ClubRecruitApply;
import com.club.entity.enums.ApplyStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团招新申请Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubRecruitApplyRepository extends JpaRepository<ClubRecruitApply, Long>, JpaSpecificationExecutor<ClubRecruitApply> {

    Page<ClubRecruitApply> findByRecruitIdAndDeletedFalseOrderByCreateTimeDesc(Long recruitId, Pageable pageable);

    Page<ClubRecruitApply> findByRecruitIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(Long recruitId, ApplyStatusEnum status, Pageable pageable);

    Page<ClubRecruitApply> findByClubIdAndDeletedFalseOrderByCreateTimeDesc(Long clubId, Pageable pageable);

    Page<ClubRecruitApply> findByUserIdAndDeletedFalseOrderByCreateTimeDesc(Long userId, Pageable pageable);

    List<ClubRecruitApply> findByRecruitIdAndStatusAndDeletedFalse(Long recruitId, ApplyStatusEnum status);

    Optional<ClubRecruitApply> findByIdAndDeletedFalse(Long id);

    Optional<ClubRecruitApply> findByUserIdAndRecruitIdAndDeletedFalse(Long userId, Long recruitId);

    boolean existsByUserIdAndRecruitIdAndDeletedFalse(Long userId, Long recruitId);

    long countByRecruitIdAndStatusAndDeletedFalse(Long recruitId, ApplyStatusEnum status);
}
