package com.bike.repository;

import com.bike.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 区域数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface AreaRepository extends JpaRepository<Area, Long>, JpaSpecificationExecutor<Area> {

    Optional<Area> findByAreaCode(String areaCode);

    List<Area> findByAreaType(String areaType);

    List<Area> findByDemandLevelGreaterThanEqual(Integer level);
}
