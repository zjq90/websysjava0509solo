package com.club.repository;

import com.club.entity.PastPresident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PastPresidentRepository extends JpaRepository<PastPresident, Long>, JpaSpecificationExecutor<PastPresident> {

    List<PastPresident> findByClubIdAndDeletedFalseOrderBySortOrderAsc(Long clubId);

    List<PastPresident> findByClubIdAndDeletedFalseOrderByTermStartDesc(Long clubId);

    Optional<PastPresident> findByIdAndDeletedFalse(Long id);

    Optional<PastPresident> findByIdAndClubIdAndDeletedFalse(Long id, Long clubId);
}
