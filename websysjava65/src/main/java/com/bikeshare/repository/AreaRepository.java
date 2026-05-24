package com.bikeshare.repository;

import com.bikeshare.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 区域数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    List<Area> findByIsActiveTrue();
}
