package com.websys.service;

import com.websys.entity.Slot;
import com.websys.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 货道服务类
 * 提供货道管理的业务逻辑，包括增删改查等操作
 */
@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    /**
     * 分页查询货道列表
     * @param deviceId 设备ID
     * @param deviceCode 设备编号（模糊查询）
     * @param productName 商品名称（模糊查询）
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页货道列表
     */
    public Page<Slot> findPage(Long deviceId, String deviceCode, String productName, Integer status, Pageable pageable) {
        return slotRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (deviceId != null) {
                predicates.add(cb.equal(root.get("deviceId"), deviceId));
            }
            
            if (deviceCode != null && !deviceCode.isEmpty()) {
                predicates.add(cb.like(root.get("deviceCode"), "%" + deviceCode + "%"));
            }
            
            if (productName != null && !productName.isEmpty()) {
                predicates.add(cb.like(root.get("productName"), "%" + productName + "%"));
            }
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    /**
     * 根据设备ID查询所有货道
     * @param deviceId 设备ID
     * @return 货道列表
     */
    public List<Slot> findByDeviceId(Long deviceId) {
        return slotRepository.findByDeviceIdOrderBySlotNo(deviceId);
    }

    /**
     * 根据设备编号查询所有货道
     * @param deviceCode 设备编号
     * @return 货道列表
     */
    public List<Slot> findByDeviceCode(String deviceCode) {
        return slotRepository.findByDeviceCodeOrderBySlotNo(deviceCode);
    }

    /**
     * 根据ID查询货道
     * @param id 货道ID
     * @return 货道对象
     */
    public Optional<Slot> findById(Long id) {
        return slotRepository.findById(id);
    }

    /**
     * 保存货道（新增/更新）
     * @param slot 货道对象
     * @return 保存后的货道对象
     */
    @Transactional
    public Slot save(Slot slot) {
        if (slot.getStock() == null) {
            slot.setStock(0);
        }
        if (slot.getCapacity() == null) {
            slot.setCapacity(20);
        }
        if (slot.getMaxCapacity() == null) {
            slot.setMaxCapacity(slot.getCapacity() != null ? slot.getCapacity() : 20);
        }
        slot.setCurrentQuantity(slot.getStock());
        if (slot.getSlotCode() == null && slot.getSlotNo() != null) {
            slot.setSlotCode(slot.getSlotNo());
        }
        if (slot.getSlotIndex() == null) {
            try {
                String no = slot.getSlotNo();
                if (no != null && no.length() > 1) {
                    slot.setSlotIndex(Integer.parseInt(no.substring(1)));
                }
            } catch (Exception ignored) {
            }
        }
        if (slot.getStock() == 0) {
            slot.setStatus(3);
        } else if (slot.getStock() > 0) {
            slot.setStatus(1);
        }
        if (slot.getId() != null) {
            slot.setUpdateTime(LocalDateTime.now());
            slot.setLastUpdateTime(LocalDateTime.now());
        } else {
            slot.setLastUpdateTime(LocalDateTime.now());
        }
        return slotRepository.save(slot);
    }

    /**
     * 删除货道
     * @param id 货道ID
     */
    @Transactional
    public void deleteById(Long id) {
        slotRepository.deleteById(id);
    }

    /**
     * 根据设备ID删除所有货道
     * @param deviceId 设备ID
     */
    @Transactional
    public void deleteByDeviceId(Long deviceId) {
        slotRepository.deleteByDeviceId(deviceId);
    }

    /**
     * 统计设备缺货货道数
     * @param deviceId 设备ID
     * @return 缺货数量
     */
    public long countOutOfStock(Long deviceId) {
        return slotRepository.countByDeviceIdAndStatus(deviceId, 3);
    }

    /**
     * 补货操作
     * @param id 货道ID
     * @param quantity 补货数量
     * @return 更新后的货道
     */
    @Transactional
    public Slot replenish(Long id, Integer quantity) {
        Optional<Slot> slotOpt = slotRepository.findById(id);
        if (slotOpt.isPresent()) {
            Slot slot = slotOpt.get();
            int newStock = Math.min(slot.getStock() + quantity, slot.getMaxCapacity() != null ? slot.getMaxCapacity() : slot.getCapacity());
            slot.setStock(newStock);
            slot.setCurrentQuantity(newStock);
            if (newStock > 0) {
                slot.setStatus(1);
            }
            slot.setUpdateTime(LocalDateTime.now());
            slot.setLastUpdateTime(LocalDateTime.now());
            return slotRepository.save(slot);
        }
        throw new RuntimeException("货道不存在");
    }

    /**
     * 出货操作
     * @param id 货道ID
     * @return 更新后的货道
     */
    @Transactional
    public Slot dispense(Long id) {
        Optional<Slot> slotOpt = slotRepository.findById(id);
        if (slotOpt.isPresent()) {
            Slot slot = slotOpt.get();
            if (slot.getStock() <= 0) {
                throw new RuntimeException("货道已缺货");
            }
            slot.setStock(slot.getStock() - 1);
            slot.setCurrentQuantity(slot.getStock());
            if (slot.getStock() == 0) {
                slot.setStatus(3);
            }
            slot.setUpdateTime(LocalDateTime.now());
            slot.setLastUpdateTime(LocalDateTime.now());
            return slotRepository.save(slot);
        }
        throw new RuntimeException("货道不存在");
    }
}
