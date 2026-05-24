package com.bikeshare.repository;

import com.bikeshare.entity.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 车辆数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {

    List<Bike> findByStatus(String status);

    List<Bike> findByAreaId(Long areaId);

    long countByStatus(String status);

    @Query("SELECT b.areaId, COUNT(b) FROM Bike b GROUP BY b.areaId")
    List<Object[]> countByArea();
}
