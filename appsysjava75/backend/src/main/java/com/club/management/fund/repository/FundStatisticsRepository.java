package com.club.management.fund.repository;

import com.club.management.fund.entity.FundStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 经费统计报表Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface FundStatisticsRepository extends JpaRepository<FundStatistics, Long>, JpaSpecificationExecutor<FundStatistics> {

    List<FundStatistics> findByClubIdAndStatisticsTypeOrderByPeriodDesc(Long clubId, Integer statisticsType);

    FundStatistics findByClubIdAndStatisticsTypeAndPeriod(Long clubId, Integer statisticsType, String period);
}
