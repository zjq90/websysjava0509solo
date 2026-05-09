package com.production.repository;

import com.production.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 设备数据访问层
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    /**
     * 根据设备编号查询
     */
    Optional<Equipment> findByEquipmentCode(String equipmentCode);

    /**
     * 根据设备类型查询
     */
    List<Equipment> findByEquipmentType(String equipmentType);

    /**
     * 根据状态查询
     */
    List<Equipment> findByStatus(String status);

    /**
     * 根据IP地址查询
     */
    Optional<Equipment> findByIpAddress(String ipAddress);

    /**
     * 检查设备编号是否存在
     */
    boolean existsByEquipmentCode(String equipmentCode);

    /**
     * 查询所有运行中的设备
     */
    @Query("SELECT e FROM Equipment e WHERE e.status = 'RUNNING' ORDER BY e.createTime DESC")
    List<Equipment> findAllRunning();

    /**
     * 查询所有在线设备
     */
    @Query("SELECT e FROM Equipment e WHERE e.status <> 'OFFLINE' ORDER BY e.createTime DESC")
    List<Equipment> findAllOnline();
}