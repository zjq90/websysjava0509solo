package com.seedinventory.service;

import com.seedinventory.entity.Inventory;
import com.seedinventory.entity.InventoryRecord;
import com.seedinventory.entity.Notification;
import com.seedinventory.entity.Seed;
import com.seedinventory.entity.Warehouse;
import com.seedinventory.repository.InventoryRecordRepository;
import com.seedinventory.repository.InventoryRepository;
import com.seedinventory.repository.NotificationRepository;
import com.seedinventory.repository.SeedRepository;
import com.seedinventory.repository.WarehouseRepository;
import com.seedinventory.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 库存管理服务类
 * 核心业务逻辑：入库、出库、库存查询、预警检测等
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private InventoryRecordRepository recordRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private SeedRepository seedRepository;
    
    @Value("${app.notification.near-expiry-days:90}")
    private int nearExpiryDays;
    
    @Value("${app.notification.low-stock-threshold:50}")
    private int lowStockThreshold;
    
    /**
     * 入库操作
     * 通过扫码批次号或手动录入完成入库
     * 
     * @param inventory 库存信息
     * @param operator 操作人
     * @param remark 备注
     * @return 入库后的库存信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Inventory inbound(Inventory inventory, String operator, String remark) {
        ValidationUtil.ValidationResult batchResult = ValidationUtil.validateBatchNo(inventory.getBatchNo());
        if (!batchResult.isSuccess()) {
            throw new RuntimeException(batchResult.getMessage());
        }
        
        ValidationUtil.ValidationResult expiryResult = ValidationUtil.validateExpiryDate(inventory.getExpiryDate());
        if (!expiryResult.isSuccess()) {
            throw new RuntimeException(expiryResult.getMessage());
        }
        
        ValidationUtil.ValidationResult rateResult = ValidationUtil.validateGerminationRate(inventory.getGerminationRate().doubleValue());
        if (!rateResult.isSuccess()) {
            throw new RuntimeException(rateResult.getMessage());
        }
        
        if (inventoryRepository.existsByBatchNo(inventory.getBatchNo())) {
            throw new RuntimeException("批次号已存在，请使用唯一的批次号");
        }
        
        if (!warehouseRepository.existsById(inventory.getWarehouseId())) {
            throw new RuntimeException("仓库不存在");
        }
        
        if (!seedRepository.existsById(inventory.getSeedId())) {
            throw new RuntimeException("种子信息不存在");
        }
        
        inventory.setStatus("NORMAL");
        inventory.setInboundTime(LocalDateTime.now());
        Inventory saved = inventoryRepository.save(inventory);
        
        InventoryRecord record = new InventoryRecord();
        record.setRecordNo(generateRecordNo("IN"));
        record.setRecordType("INBOUND");
        record.setBatchNo(saved.getBatchNo());
        record.setWarehouseId(saved.getWarehouseId());
        record.setSeedId(saved.getSeedId());
        record.setQuantity(saved.getQuantity());
        record.setUnit(saved.getUnit());
        record.setBeforeQuantity(BigDecimal.ZERO);
        record.setAfterQuantity(saved.getQuantity());
        record.setOperator(operator);
        record.setRemark(remark);
        recordRepository.save(record);
        
        checkAndCreateNotification(saved);
        
        return saved;
    }
    
    /**
     * 出库操作
     * 通过扫码批次号完成出库
     * 
     * @param batchNo 批次号
     * @param quantity 出库数量
     * @param operator 操作人
     * @param customerId 客户ID（可选）
     * @param relatedNo 关联单号（可选）
     * @param remark 备注
     * @return 出库后的库存信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Inventory outbound(String batchNo, BigDecimal quantity, String operator, 
                              Long customerId, String relatedNo, String remark) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByBatchNo(batchNo);
        if (!inventoryOpt.isPresent()) {
            throw new RuntimeException("库存记录不存在，批次号：" + batchNo);
        }
        
        Inventory inventory = inventoryOpt.get();
        
        if ("EXPIRED".equals(inventory.getStatus())) {
            throw new RuntimeException("该批次已过期，无法出库");
        }
        
        if (inventory.getQuantity().compareTo(quantity) < 0) {
            throw new RuntimeException("库存不足，当前库存：" + inventory.getQuantity());
        }
        
        BigDecimal beforeQty = inventory.getQuantity();
        BigDecimal afterQty = beforeQty.subtract(quantity);
        inventory.setQuantity(afterQty);
        
        if (afterQty.compareTo(BigDecimal.ZERO) == 0) {
            inventory.setStatus("LOW_STOCK");
        } else if (afterQty.compareTo(new BigDecimal(lowStockThreshold)) < 0) {
            inventory.setStatus("LOW_STOCK");
            createLowStockNotification(inventory);
        }
        
        Inventory saved = inventoryRepository.save(inventory);
        
        InventoryRecord record = new InventoryRecord();
        record.setRecordNo(generateRecordNo("OUT"));
        record.setRecordType("OUTBOUND");
        record.setBatchNo(batchNo);
        record.setWarehouseId(inventory.getWarehouseId());
        record.setSeedId(inventory.getSeedId());
        record.setQuantity(quantity);
        record.setUnit(inventory.getUnit());
        record.setBeforeQuantity(beforeQty);
        record.setAfterQuantity(afterQty);
        record.setCustomerId(customerId);
        record.setRelatedNo(relatedNo);
        record.setOperator(operator);
        record.setRemark(remark);
        recordRepository.save(record);
        
        return saved;
    }
    
    /**
     * 根据批次号查询库存
     * 用于扫码后快速查询
     */
    public Optional<Inventory> findByBatchNo(String batchNo) {
        return inventoryRepository.findByBatchNo(batchNo);
    }
    
    /**
     * 查询指定仓库的所有库存
     */
    public List<Inventory> findByWarehouseId(Long warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId);
    }
    
    /**
     * 查询所有库存
     */
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
    
    /**
     * 检测并创建近效期预警
     */
    @Transactional(rollbackFor = Exception.class)
    public int checkNearExpiry() {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(nearExpiryDays);
        
        List<Inventory> nearExpiryList = inventoryRepository.findNearExpiry(today, targetDate);
        int count = 0;
        
        for (Inventory inventory : nearExpiryList) {
            if (!"NEAR_EXPIRY".equals(inventory.getStatus())) {
                inventory.setStatus("NEAR_EXPIRY");
                inventoryRepository.save(inventory);
                createNearExpiryNotification(inventory);
                count++;
            }
        }
        
        List<Inventory> expiredList = inventoryRepository.findExpired(today);
        for (Inventory inventory : expiredList) {
            if (!"EXPIRED".equals(inventory.getStatus())) {
                inventory.setStatus("EXPIRED");
                inventoryRepository.save(inventory);
                createExpiredNotification(inventory);
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 创建近效期预警通知
     */
    private void createNearExpiryNotification(Inventory inventory) {
        Notification notification = new Notification();
        notification.setNotificationType("NEAR_EXPIRY");
        notification.setTitle("近效期预警");
        notification.setContent("批次号[" + inventory.getBatchNo() + "]即将过期，保质期至：" + inventory.getExpiryDate() + "，请及时处理！");
        notification.setBatchNo(inventory.getBatchNo());
        notification.setWarehouseId(inventory.getWarehouseId());
        notification.setSeedId(inventory.getSeedId());
        notification.setPriority("HIGH");
        notificationRepository.save(notification);
    }
    
    /**
     * 创建已过期通知
     */
    private void createExpiredNotification(Inventory inventory) {
        Notification notification = new Notification();
        notification.setNotificationType("NEAR_EXPIRY");
        notification.setTitle("已过期警告");
        notification.setContent("批次号[" + inventory.getBatchNo() + "]已过期，保质期至：" + inventory.getExpiryDate() + "，请立即处理！");
        notification.setBatchNo(inventory.getBatchNo());
        notification.setWarehouseId(inventory.getWarehouseId());
        notification.setSeedId(inventory.getSeedId());
        notification.setPriority("HIGH");
        notificationRepository.save(notification);
    }
    
    /**
     * 创建缺货提醒通知
     */
    private void createLowStockNotification(Inventory inventory) {
        Notification notification = new Notification();
        notification.setNotificationType("LOW_STOCK");
        notification.setTitle("缺货提醒");
        notification.setContent("批次号[" + inventory.getBatchNo() + "]库存不足，当前数量：" + inventory.getQuantity() + "，请及时补货！");
        notification.setBatchNo(inventory.getBatchNo());
        notification.setWarehouseId(inventory.getWarehouseId());
        notification.setSeedId(inventory.getSeedId());
        notification.setPriority("MEDIUM");
        notificationRepository.save(notification);
    }
    
    /**
     * 检查并创建通知
     */
    private void checkAndCreateNotification(Inventory inventory) {
        LocalDate today = LocalDate.now();
        LocalDate nearExpiryDate = today.plusDays(nearExpiryDays);
        
        if (inventory.getExpiryDate().isBefore(nearExpiryDate)) {
            inventory.setStatus("NEAR_EXPIRY");
            createNearExpiryNotification(inventory);
        }
    }
    
    /**
     * 生成记录编号
     */
    private String generateRecordNo(String prefix) {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return prefix + dateStr + random;
    }
    
    /**
     * 获取所有仓库
     */
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findByStatus("ACTIVE");
    }
    
    /**
     * 获取所有种子
     */
    public List<Seed> getAllSeeds() {
        return seedRepository.findByStatus("ACTIVE");
    }
    
    /**
     * 查询出入库记录
     */
    public List<InventoryRecord> getRecordsByBatchNo(String batchNo) {
        return recordRepository.findByBatchNoOrderByRecordTimeDesc(batchNo);
    }
}
