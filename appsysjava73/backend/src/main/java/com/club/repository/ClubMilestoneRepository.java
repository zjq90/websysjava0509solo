package com.club.repository;

import com.club.entity.ClubMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubMilestoneRepository extends JpaRepository<ClubMilestone, Long>, JpaSpecificationExecutor<ClubMilestone> {

    List<ClubMilestone> findByClubIdAndDeletedFalseOrderByEventDateDescSortOrderAsc(Long clubId);

    List<ClubMilestone> findByClubIdAndDeletedFalseOrderByEventDateDesc(Long clubId);

    Optional<ClubMilestone> findByIdAndDeletedFalse(Long id);

    Optional<ClubMilestone> findByIdAndClubIdAndDeletedFalse(Long id, Long clubId);
}
