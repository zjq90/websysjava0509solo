package com.broadband.repository;

import com.broadband.entity.BroadbandNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 宽带号码数据访问层
 * 
 * @author broadband
 * @version 1.0.0
 */
@Repository
public interface BroadbandNumberRepository extends JpaRepository<BroadbandNumber, Long> {

    List<BroadbandNumber> findByStatusAndRegionOrderByNumber(Integer status, String region);

    List<BroadbandNumber> findByStatusAndTypeAndRegionOrderByNumber(Integer status, Integer type, String region);

    @Query("SELECT b FROM BroadbandNumber b WHERE b.status = 1 AND b.number LIKE %:pattern% ORDER BY b.number")
    List<BroadbandNumber> findByNumberPattern(String pattern);

    boolean existsByNumber(String number);
}
