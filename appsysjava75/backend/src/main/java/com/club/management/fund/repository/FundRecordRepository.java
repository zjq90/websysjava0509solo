package com.club.management.fund.repository;

import com.club.management.fund.entity.FundRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 经费记录Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface FundRecordRepository extends JpaRepository<FundRecord, Long>, JpaSpecificationExecutor<FundRecord> {

    Page<FundRecord> findByClubIdOrderByCreateTimeDesc(Long clubId, Pageable pageable);

    Page<FundRecord> findByClubIdAndTypeOrderByCreateTimeDesc(Long clubId, Integer type, Pageable pageable);

    List<FundRecord> findByClubIdAndStatusOrderByCreateTimeAsc(Long clubId, Integer status);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FundRecord f WHERE f.clubId = :clubId AND f.type = 1 AND f.status = 1")
    Double sumIncomeByClubId(@Param("clubId") Long clubId);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FundRecord f WHERE f.clubId = :clubId AND f.type = 0 AND f.status = 1")
    Double sumExpenseByClubId(@Param("clubId") Long clubId);

    @Query("SELECT f.category, SUM(f.amount) FROM FundRecord f WHERE f.clubId = :clubId AND f.type = :type AND f.status = 1 " +
           "AND f.createTime BETWEEN :startTime AND :endTime GROUP BY f.category")
    List<Object[]> sumByCategoryAndTimeRange(@Param("clubId") Long clubId, @Param("type") Integer type,
                                             @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    FundRecord findTopByClubIdOrderByIdDesc(Long clubId);
}
