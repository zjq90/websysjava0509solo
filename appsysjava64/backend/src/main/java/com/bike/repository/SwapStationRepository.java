package com.bike.repository;

import com.bike.entity.SwapStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 换电站数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface SwapStationRepository extends JpaRepository<SwapStation, Long>, JpaSpecificationExecutor<SwapStation> {

    Optional<SwapStation> findByStationNo(String stationNo);

    List<SwapStation> findByStatus(String status);

    List<SwapStation> findByAvailableBatteriesGreaterThanEqual(Integer count);
}
