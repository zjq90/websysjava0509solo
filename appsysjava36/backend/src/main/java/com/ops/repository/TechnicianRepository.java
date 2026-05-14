package com.ops.repository;

import com.ops.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 装维人员数据访问接口
 * 
 * @author ops-admin
 */
@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long>, JpaSpecificationExecutor<Technician> {

    /**
     * 查找在线且未满载的装维人员
     */
    List<Technician> findByStatusAndCurrentLoadLessThan(String status, Integer maxLoad);

    /**
     * 根据工作区域查找装维人员
     */
    List<Technician> findByWorkAreaContainingAndStatus(String workArea, String status);

    /**
     * 查找评分最高的装维人员
     */
    @Query("SELECT t FROM Technician t WHERE t.status = 'ONLINE' AND t.currentLoad < t.maxLoad ORDER BY t.rating DESC")
    List<Technician> findTopRatedTechnicians();
}
