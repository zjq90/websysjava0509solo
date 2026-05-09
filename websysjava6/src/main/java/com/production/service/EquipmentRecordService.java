package com.production.service;

import com.production.entity.EquipmentRecord;
import com.production.repository.EquipmentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 设备生产记录业务逻辑层
 * 用于处理设备系统传来的数据
 */
@Service
@Transactional
public class EquipmentRecordService {

    @Autowired
    private EquipmentRecordRepository equipmentRecordRepository;

    /**
     * 分页查询所有记录
     */
    public Page<EquipmentRecord> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return equipmentRecordRepository.findAll(pageable);
    }

    /**
     * 根据设备ID分页查询
     */
    public Page<EquipmentRecord> findByEquipmentIdPage(Long equipmentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recordTime"));
        if (equipmentId == null) {
            return equipmentRecordRepository.findAll(pageable);
        }
        return equipmentRecordRepository.findByEquipmentId(equipmentId, pageable);
    }

    /**
     * 查询所有记录
     */
    public List<EquipmentRecord> findAll() {
        return equipmentRecordRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    /**
     * 根据ID查询
     */
    public Optional<EquipmentRecord> findById(Long id) {
        return equipmentRecordRepository.findById(id);
    }

    /**
     * 保存设备记录
     */
    public EquipmentRecord save(EquipmentRecord record) {
        return equipmentRecordRepository.save(record);
    }

    /**
     * 删除记录
     */
    public void deleteById(Long id) {
        equipmentRecordRepository.deleteById(id);
    }

    /**
     * 根据设备ID查询历史记录
     */
    public List<EquipmentRecord> findByEquipmentId(Long equipmentId) {
        return equipmentRecordRepository.findByEquipmentIdOrderByRecordTimeDesc(equipmentId);
    }

    /**
     * 根据生产计划ID查询
     */
    public List<EquipmentRecord> findByPlanId(Long planId) {
        return equipmentRecordRepository.findByProductionPlanIdOrderByRecordTimeDesc(planId);
    }

    /**
     * 根据加工记录ID查询
     */
    public List<EquipmentRecord> findByProcessingRecordId(Long recordId) {
        return equipmentRecordRepository.findByProcessingRecordIdOrderByRecordTimeDesc(recordId);
    }

    /**
     * 查询设备最新记录
     */
    public Optional<EquipmentRecord> findLatestByEquipmentId(Long equipmentId) {
        List<EquipmentRecord> records = equipmentRecordRepository.findLatestByEquipmentId(equipmentId);
        return records.isEmpty() ? Optional.empty() : Optional.of(records.get(0));
    }

    /**
     * 查询指定时间范围内的记录
     */
    public List<EquipmentRecord> findByTimeRange(Long equipmentId, LocalDateTime startTime, LocalDateTime endTime) {
        return equipmentRecordRepository.findByTimeRange(equipmentId, startTime, endTime);
    }

    /**
     * 查询故障记录
     */
    public List<EquipmentRecord> findAllFault() {
        return equipmentRecordRepository.findAllFault();
    }

    /**
     * 接收设备推送的数据（模拟硬件通信接口）
     */
    public EquipmentRecord receiveEquipmentData(EquipmentRecord record) {
        record.setDataSource("AUTO");
        if (record.getRecordTime() == null) {
            record.setRecordTime(LocalDateTime.now());
        }
        return equipmentRecordRepository.save(record);
    }
}