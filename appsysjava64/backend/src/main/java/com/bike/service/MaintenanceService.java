package com.bike.service;

import com.bike.entity.FaultRecord;
import com.bike.entity.RepairOrder;
import com.bike.entity.SparePart;
import com.bike.entity.SparePartLog;
import com.bike.entity.Bike;
import com.bike.repository.FaultRecordRepository;
import com.bike.repository.RepairOrderRepository;
import com.bike.repository.SparePartRepository;
import com.bike.repository.SparePartLogRepository;
import com.bike.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 车辆维护服务
 * 
 * @author bike-sharing
 */
@Service
public class MaintenanceService {

    @Autowired
    private FaultRecordRepository faultRecordRepository;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Autowired
    private SparePartRepository sparePartRepository;

    @Autowired
    private SparePartLogRepository sparePartLogRepository;

    @Autowired
    private BikeRepository bikeRepository;

    public List<FaultRecord> getAllFaultRecords() {
        return faultRecordRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<FaultRecord> getFaultRecordsByBikeId(Long bikeId) {
        return faultRecordRepository.findByBikeId(bikeId);
    }

    public List<FaultRecord> getFaultRecordsByStatus(String status) {
        return faultRecordRepository.findByStatus(status);
    }

    public FaultRecord getFaultRecordById(Long id) {
        return faultRecordRepository.findById(id).orElse(null);
    }

    @Transactional
    public FaultRecord createFaultRecord(FaultRecord record) {
        String recordNo = "FR" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        record.setRecordNo(recordNo);
        if (record.getStatus() == null) {
            record.setStatus("REPORTED");
        }
        FaultRecord saved = faultRecordRepository.save(record);
        
        Bike bike = bikeRepository.findById(record.getBikeId()).orElse(null);
        if (bike != null) {
            bike.setStatus("FAULT");
            bike.setFaultType(record.getFaultType());
            bike.setFaultDescription(record.getFaultDescription());
            bikeRepository.save(bike);
        }
        
        return saved;
    }

    @Transactional
    public FaultRecord updateFaultRecord(Long id, FaultRecord record) {
        FaultRecord existing = faultRecordRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        if (record.getStatus() != null) existing.setStatus(record.getStatus());
        if (record.getFaultType() != null) existing.setFaultType(record.getFaultType());
        if (record.getFaultDescription() != null) existing.setFaultDescription(record.getFaultDescription());
        return faultRecordRepository.save(existing);
    }

    @Cacheable(value = "repairOrders", key = "'all'")
    public List<RepairOrder> getAllRepairOrders() {
        return repairOrderRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<RepairOrder> getSortedRepairOrdersByPriority() {
        List<RepairOrder> orders = repairOrderRepository.findByStatusIn(Arrays.asList("PENDING", "ACCEPTED", "IN_PROGRESS"));
        orders.sort((a, b) -> {
            Map<String, Integer> priorityMap = new HashMap<>();
            priorityMap.put("URGENT", 1);
            priorityMap.put("HIGH", 2);
            priorityMap.put("MEDIUM", 3);
            priorityMap.put("LOW", 4);
            
            int priorityCompare = priorityMap.getOrDefault(a.getPriority(), 5) 
                    - priorityMap.getOrDefault(b.getPriority(), 5);
            if (priorityCompare != 0) {
                return priorityCompare;
            }
            return a.getCreateTime().compareTo(b.getCreateTime());
        });
        return orders;
    }

    public List<RepairOrder> getRepairOrdersByStaffId(Long staffId) {
        return repairOrderRepository.findByStaffId(staffId);
    }

    public List<RepairOrder> getRepairOrdersByStatus(String status) {
        return repairOrderRepository.findByStatus(status);
    }

    public List<RepairOrder> getRepairOrdersByBikeId(Long bikeId) {
        return repairOrderRepository.findByBikeId(bikeId);
    }

    public RepairOrder getRepairOrderById(Long id) {
        return repairOrderRepository.findById(id).orElse(null);
    }

    public RepairOrder getRepairOrderByOrderNo(String orderNo) {
        return repairOrderRepository.findByOrderNo(orderNo).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "repairOrders", allEntries = true)
    public RepairOrder createRepairOrder(RepairOrder order) {
        String orderNo = "RO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        order.setOrderNo(orderNo);
        order.setTaskNo("T" + System.currentTimeMillis());
        if (order.getStatus() == null) {
            order.setStatus("PENDING");
        }
        if (order.getPriority() == null) {
            order.setPriority("MEDIUM");
        }
        return repairOrderRepository.save(order);
    }

    @Transactional
    @CacheEvict(value = "repairOrders", allEntries = true)
    public RepairOrder createRepairOrderFromFault(Long faultRecordId) {
        FaultRecord fault = faultRecordRepository.findById(faultRecordId).orElse(null);
        if (fault == null) {
            return null;
        }
        
        Bike bike = bikeRepository.findById(fault.getBikeId()).orElse(null);
        
        RepairOrder order = new RepairOrder();
        order.setOrderNo("RO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        order.setTaskNo("T" + System.currentTimeMillis());
        order.setBikeId(fault.getBikeId());
        order.setBikeNo(fault.getBikeNo());
        order.setFaultType(fault.getFaultType());
        order.setFaultDescription(fault.getFaultDescription());
        
        if (bike != null) {
            order.setBikeLocation(bike.getLocation());
            order.setBikeLongitude(bike.getLongitude());
            order.setBikeLatitude(bike.getLatitude());
        }
        
        String priority = "MEDIUM";
        if ("刹车故障".equals(fault.getFaultType()) || "电池故障".equals(fault.getFaultType())) {
            priority = "URGENT";
        } else if ("链条故障".equals(fault.getFaultType()) || "轮胎故障".equals(fault.getFaultType())) {
            priority = "HIGH";
        }
        order.setPriority(priority);
        order.setStatus("PENDING");
        
        fault.setStatus("PROCESSING");
        faultRecordRepository.save(fault);
        
        return repairOrderRepository.save(order);
    }

    @Transactional
    @CacheEvict(value = "repairOrders", allEntries = true)
    public RepairOrder acceptRepairOrder(Long orderId, Long staffId, String staffName) {
        RepairOrder order = repairOrderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }
        order.setStatus("ACCEPTED");
        order.setStaffId(staffId);
        order.setStaffName(staffName);
        order.setAcceptTime(LocalDateTime.now());
        return repairOrderRepository.save(order);
    }

    @Transactional
    @CacheEvict(value = "repairOrders", allEntries = true)
    public RepairOrder startRepair(Long orderId) {
        RepairOrder order = repairOrderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }
        order.setStatus("IN_PROGRESS");
        order.setStartRepairTime(LocalDateTime.now());
        return repairOrderRepository.save(order);
    }

    @Transactional
    @CacheEvict(value = "repairOrders", allEntries = true)
    public RepairOrder completeRepair(Long orderId, String repairDescription, String usedParts, Float cost) {
        RepairOrder order = repairOrderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }
        order.setStatus("COMPLETED");
        order.setCompleteTime(LocalDateTime.now());
        order.setRepairDescription(repairDescription);
        order.setUsedParts(usedParts);
        order.setRepairCost(cost);
        
        if (order.getStartRepairTime() != null) {
            long minutes = ChronoUnit.MINUTES.between(order.getStartRepairTime(), order.getCompleteTime());
            order.setRepairDuration((int) minutes);
        }
        
        Bike bike = bikeRepository.findById(order.getBikeId()).orElse(null);
        if (bike != null) {
            bike.setStatus("AVAILABLE");
            bike.setFaultType(null);
            bike.setFaultDescription(null);
            bike.setLastMaintenanceTime(LocalDateTime.now());
            bikeRepository.save(bike);
        }
        
        List<FaultRecord> records = faultRecordRepository.findByBikeId(order.getBikeId());
        for (FaultRecord record : records) {
            if ("PROCESSING".equals(record.getStatus())) {
                record.setStatus("RESOLVED");
                faultRecordRepository.save(record);
            }
        }
        
        return repairOrderRepository.save(order);
    }

    @Transactional
    @CacheEvict(value = "repairOrders", allEntries = true)
    public void deleteRepairOrder(Long id) {
        repairOrderRepository.deleteById(id);
    }

    @Cacheable(value = "spareParts", key = "'all'")
    public List<SparePart> getAllSpareParts() {
        return sparePartRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<SparePart> getSparePartsByCategory(String category) {
        return sparePartRepository.findByCategory(category);
    }

    public List<SparePart> getLowStockParts() {
        return sparePartRepository.findAll().stream()
                .filter(p -> p.getStockQuantity() <= p.getMinStock())
                .collect(java.util.stream.Collectors.toList());
    }

    public SparePart getSparePartById(Long id) {
        return sparePartRepository.findById(id).orElse(null);
    }

    public SparePart getSparePartByCode(String partCode) {
        return sparePartRepository.findByPartCode(partCode).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "spareParts", allEntries = true)
    public SparePart createSparePart(SparePart part) {
        if (part.getStatus() == null) {
            part.setStatus("ACTIVE");
        }
        return sparePartRepository.save(part);
    }

    @Transactional
    @CacheEvict(value = "spareParts", allEntries = true)
    public SparePart updateSparePart(Long id, SparePart part) {
        SparePart existing = sparePartRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        if (part.getPartName() != null) existing.setPartName(part.getPartName());
        if (part.getCategory() != null) existing.setCategory(part.getCategory());
        if (part.getSpecification() != null) existing.setSpecification(part.getSpecification());
        if (part.getUnitPrice() != null) existing.setUnitPrice(part.getUnitPrice());
        if (part.getStockQuantity() != null) existing.setStockQuantity(part.getStockQuantity());
        if (part.getMinStock() != null) existing.setMinStock(part.getMinStock());
        if (part.getStatus() != null) existing.setStatus(part.getStatus());
        if (part.getSupplier() != null) existing.setSupplier(part.getSupplier());
        return sparePartRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "spareParts", allEntries = true)
    public SparePart updateStock(Long partId, Integer quantity, String operationType, Long operatorId, String operatorName, String remark) {
        SparePart part = sparePartRepository.findById(partId).orElse(null);
        if (part == null) {
            return null;
        }
        
        int beforeQuantity = part.getStockQuantity();
        int afterQuantity;
        
        if ("IN".equals(operationType)) {
            afterQuantity = beforeQuantity + quantity;
        } else if ("OUT".equals(operationType)) {
            afterQuantity = beforeQuantity - quantity;
            if (afterQuantity < 0) {
                throw new IllegalArgumentException("库存不足");
            }
        } else {
            throw new IllegalArgumentException("无效的操作类型");
        }
        
        part.setStockQuantity(afterQuantity);
        SparePart saved = sparePartRepository.save(part);
        
        SparePartLog log = new SparePartLog();
        log.setPartId(partId);
        log.setPartCode(part.getPartCode());
        log.setPartName(part.getPartName());
        log.setOperationType(operationType);
        log.setQuantity(quantity);
        log.setBeforeQuantity(beforeQuantity);
        log.setAfterQuantity(afterQuantity);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setRemark(remark);
        sparePartLogRepository.save(log);
        
        return saved;
    }

    public List<SparePartLog> getSparePartLogs(Long partId) {
        if (partId != null) {
            return sparePartLogRepository.findByPartId(partId);
        }
        return sparePartLogRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }
}
