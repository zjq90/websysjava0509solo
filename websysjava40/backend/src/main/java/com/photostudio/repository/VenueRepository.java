package com.photostudio.repository;

import com.photostudio.entity.Venue;
import com.photostudio.entity.Venue.VenueStatus;
import com.photostudio.entity.Venue.VenueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 场地数据访问接口
 * 提供场地相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    /**
     * 根据场地编号查询场地
     * @param venueNo 场地编号
     * @return 场地信息
     */
    Venue findByVenueNo(String venueNo);

    /**
     * 根据类型查询场地
     * @param type 场地类型
     * @return 场地列表
     */
    List<Venue> findByType(VenueType type);

    /**
     * 根据状态查询场地
     * @param status 场地状态
     * @return 场地列表
     */
    List<Venue> findByStatus(VenueStatus status);

    /**
     * 查询可用的场地
     * @return 场地列表
     */
    List<Venue> findByAvailableTrue();

    /**
     * 根据名称模糊查询场地
     * @param name 名称关键词
     * @return 场地列表
     */
    List<Venue> findByNameContaining(String name);
}
