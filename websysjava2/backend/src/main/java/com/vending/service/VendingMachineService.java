package com.vending.service;

import com.vending.entity.Slot;
import com.vending.entity.VendingMachine;
import com.vending.repository.SlotRepository;
import com.vending.repository.VendingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 售货机服务类
 * 提供售货机设备的增删改查功能
 */
@Service
public class VendingMachineService {
    
    @Autowired
    private VendingMachineRepository machineRepository;
    
    @Autowired
    private SlotRepository slotRepository;
    
    /**
     * 获取所有售货机
     */
    public List<VendingMachine> findAll() {
        return machineRepository.findAll();
    }
    
    /**
     * 根据ID查询售货机
     */
    public Optional<VendingMachine> findById(Long id) {
        return machineRepository.findById(id);
    }
    
    /**
     * 保存售货机
     */
    @Transactional
    public VendingMachine save(VendingMachine machine) {
        if (machineRepository.existsByMachineCode(machine.getMachineCode())) {
            throw new RuntimeException("设备编号已存在");
        }
        
        VendingMachine savedMachine = machineRepository.save(machine);
        
        int slotCount = machine.getSlotCount() != null ? machine.getSlotCount() : 20;
        for (int i = 1; i <= slotCount; i++) {
            Slot slot = new Slot();
            slot.setMachine(savedMachine);
            slot.setSlotNumber(i);
            slot.setCurrentStock(0);
            slot.setMaxCapacity(10);
            slot.setEnabled(true);
            slotRepository.save(slot);
        }
        
        return savedMachine;
    }
    
    /**
     * 更新售货机
     */
    @Transactional
    public VendingMachine update(Long id, VendingMachine machine) {
        VendingMachine existing = machineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("设备不存在"));
        
        if (!existing.getMachineCode().equals(machine.getMachineCode())
            && machineRepository.existsByMachineCode(machine.getMachineCode())) {
            throw new RuntimeException("设备编号已存在");
        }
        
        existing.setName(machine.getName());
        existing.setMachineCode(machine.getMachineCode());
        existing.setLocation(machine.getLocation());
        existing.setIpAddress(machine.getIpAddress());
        existing.setDescription(machine.getDescription());
        existing.setStatus(machine.getStatus());
        
        return machineRepository.save(existing);
    }
    
    /**
     * 删除售货机
     */
    @Transactional
    public void deleteById(Long id) {
        if (!machineRepository.existsById(id)) {
            throw new RuntimeException("设备不存在");
        }
        machineRepository.deleteById(id);
    }
    
    /**
     * 更新设备状态
     */
    @Transactional
    public VendingMachine updateStatus(Long id, String status) {
        VendingMachine machine = machineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("设备不存在"));
        
        machine.setStatus(status);
        if ("ONLINE".equals(status)) {
            machine.setLastOnlineTime(LocalDateTime.now());
        }
        
        return machineRepository.save(machine);
    }
    
    /**
     * 根据状态查询设备
     */
    public List<VendingMachine> findByStatus(String status) {
        return machineRepository.findByStatus(status);
    }
}
