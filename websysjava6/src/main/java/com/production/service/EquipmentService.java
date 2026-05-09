package com.production.service;

import com.production.entity.Equipment;
import com.production.repository.EquipmentRepository;
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
 * 设备业务逻辑层
 */
@Service
@Transactional
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    /**
     * 分页查询所有设备
     */
    public Page<Equipment> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return equipmentRepository.findAll(pageable);
    }

    /**
     * 查询所有设备
     */
    public List<Equipment> findAll() {
        return equipmentRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    /**
     * 根据ID查询
     */
    public Optional<Equipment> findById(Long id) {
        return equipmentRepository.findById(id);
    }

    /**
     * 根据设备编号查询
     */
    public Optional<Equipment> findByEquipmentCode(String equipmentCode) {
        return equipmentRepository.findByEquipmentCode(equipmentCode);
    }

    /**
     * 保存设备
     */
    public Equipment save(Equipment equipment) {
        if (equipment.getId() == null) {
            // 新增设备，检查编号是否已存在
            if (equipmentRepository.existsByEquipmentCode(equipment.getEquipmentCode())) {
                throw new RuntimeException("设备编号已存在：" + equipment.getEquipmentCode());
            }
            if (equipment.getStatus() == null) {
                equipment.setStatus("OFFLINE");
            }
        }
        equipment.setUpdateTime(LocalDateTime.now());
        return equipmentRepository.save(equipment);
    }

    /**
     * 删除设备
     */
    public void deleteById(Long id) {
        equipmentRepository.deleteById(id);
    }

    /**
     * 更新设备状态
     */
    public Equipment updateStatus(Long id, String status) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(id);
        if (equipmentOpt.isPresent()) {
            Equipment equipment = equipmentOpt.get();
            equipment.setStatus(status);
            equipment.setUpdateTime(LocalDateTime.now());
            return equipmentRepository.save(equipment);
        }
        return null;
    }

    /**
     * 查询所有运行中设备
     */
    public List<Equipment> findAllRunning() {
        return equipmentRepository.findAllRunning();
    }

    /**
     * 查询所有在线设备
     */
    public List<Equipment> findAllOnline() {
        return equipmentRepository.findAllOnline();
    }

    /**
     * 根据类型查询
     */
    public List<Equipment> findByType(String type) {
        return equipmentRepository.findByEquipmentType(type);
    }

    /**
     * 根据状态查询
     */
    public List<Equipment> findByStatus(String status) {
        return equipmentRepository.findByStatus(status);
    }
}