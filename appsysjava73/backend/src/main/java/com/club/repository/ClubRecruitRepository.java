package com.club.repository;

import com.club.entity.ClubRecruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团招新Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubRecruitRepository extends JpaRepository<ClubRecruit, Long>, JpaSpecificationExecutor<ClubRecruit> {

    Page<ClubRecruit> findByStatusAndDeletedFalseOrderByCreateTimeDesc(Integer status, Pageable pageable);

    Page<ClubRecruit> findByClubIdAndDeletedFalseOrderByCreateTimeDesc(Long clubId, Pageable pageable);

    List<ClubRecruit> findByClubIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(Long clubId, Integer status);

    Optional<ClubRecruit> findByIdAndDeletedFalse(Long id);

    Optional<ClubRecruit> findByClubIdAndStatusAndDeletedFalse(Long clubId, Integer status);

    boolean existsByClubIdAndStatusAndDeletedFalse(Long clubId, Integer status);
}
