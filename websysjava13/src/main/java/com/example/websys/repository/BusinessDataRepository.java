package com.example.websys.repository;

import com.example.websys.entity.BusinessData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 经营数据数据访问接口
 * 提供经营数据的增删改查功能
 */
@Repository
public interface BusinessDataRepository extends JpaRepository<BusinessData, Long> {

    Optional<BusinessData> findByStatCodeAndDataDate(String statCode, LocalDate dataDate);

    List<BusinessData> findByDataDateOrderByIdAsc(LocalDate dataDate);

    @Query("SELECT b FROM BusinessData b WHERE b.statCode = :statCode AND b.dataDate >= :startDate AND b.dataDate <= :endDate ORDER BY b.dataDate DESC")
    List<BusinessData> findByStatCodeAndDateRange(@Param("statCode") String statCode,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM BusinessData b WHERE b.statCode IN :statCodes AND b.dataDate = :date ORDER BY b.statCode")
    List<BusinessData> findByStatCodesAndDate(@Param("statCodes") List<String> statCodes,
                                               @Param("date") LocalDate date);
}
