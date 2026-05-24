package com.bikeshare.repository;

import com.bikeshare.entity.CostRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 成本记录数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface CostRecordRepository extends JpaRepository<CostRecord, Long> {

    List<CostRecord> findByCostType(String costType);

    List<CostRecord> findByCostCategory(String costCategory);

    @Query("SELECT c.costType, COALESCE(SUM(c.amount), 0) FROM CostRecord c WHERE c.costDate >= :startDate GROUP BY c.costType")
    List<Object[]> sumAmountByType(LocalDate startDate);

    @Query("SELECT c.costCategory, COALESCE(SUM(c.amount), 0) FROM CostRecord c WHERE c.costDate >= :startDate GROUP BY c.costCategory")
    List<Object[]> sumAmountByCategory(LocalDate startDate);
}
