package com.culturalrelic.repository;

import com.culturalrelic.entity.IotDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物联网设备数据访问层
 */
@Repository
public interface IotDeviceRepository extends JpaRepository<IotDevice, Long>, JpaSpecificationExecutor<IotDevice> {

    /**
     * 根据设备编号查询
     */
    IotDevice findByDeviceNo(String deviceNo);

    /**
     * 根据NFC标签ID查询
     */
    IotDevice findByNfcTagId(String nfcTagId);

    /**
     * 根据文物ID查询绑定的设备
     */
    List<IotDevice> findByRelicId(Long relicId);

    /**
     * 根据状态查询
     */
    List<IotDevice> findByStatus(Integer status);

    /**
     * 查询告警的设备
     */
    List<IotDevice> findByAlarmStatus(Integer alarmStatus);

    /**
     * 逻辑删除
     */
    @Modifying
    @Query("UPDATE IotDevice d SET d.deleted = 1 WHERE d.id = :id")
    int logicDelete(@Param("id") Long id);
}
