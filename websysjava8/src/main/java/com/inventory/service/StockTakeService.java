package com.inventory.service;

import com.inventory.entity.Inventory;
import com.inventory.entity.SeedBatch;
import com.inventory.entity.StockTake;
import com.inventory.entity.StockTakeItem;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.SeedBatchRepository;
import com.inventory.repository.StockTakeItemRepository;
import com.inventory.repository.StockTakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 盘点管理Service类
 * 处理库存盘点的全流程：创建 -> 开始盘点 -> 完成盘点
 */
@Service
public class StockTakeService {

    @Autowired
    private StockTakeRepository stockTakeRepository;

    @Autowired
    private StockTakeItemRepository stockTakeItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SeedBatchRepository seedBatchRepository;

    /**
     * 查询所有盘点单
     */
    public List<StockTake> findAll() {
        return stockTakeRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public StockTake findById(Long id) {
        Optional<StockTake> optional = stockTakeRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 根据单号查询
     */
    public StockTake findByOrderNo(String orderNo) {
        return stockTakeRepository.findByOrderNo(orderNo);
    }

    /**
     * 根据状态查询
     */
    public List<StockTake> findByStatus(Integer status) {
        return stockTakeRepository.findByStatus(status);
    }

    /**
     * 创建盘点单
     */
    @Transactional(rollbackFor = Exception.class)
    public StockTake createStockTake(StockTake stockTake) {
        // 生成盘点单号
        String orderNo = "PD" + System.currentTimeMillis();
        stockTake.setOrderNo(orderNo);
        stockTake.setStatus(0);

        StockTake saved = stockTakeRepository.save(stockTake);

        // 自动生成盘点明细（拉取该位置所有库存）
        List<Inventory> inventoryList = inventoryRepository.findByInventoryTypeAndLocationId(
                stockTake.getLocationType(),
                stockTake.getLocationId()
        );

        for (Inventory inventory : inventoryList) {
            StockTakeItem item = new StockTakeItem();
            item.setTakeId(saved.getId());
            item.setInventoryId(inventory.getId());
            item.setBatchId(inventory.getBatchId());
            item.setBookQuantity(inventory.getQuantity());
            item.setActualQuantity(0);
            item.setDiffQuantity(0 - inventory.getQuantity());
            item.setStatus(0);

            // 获取单价
            SeedBatch batch = seedBatchRepository.findById(inventory.getBatchId()).orElse(null);
            if (batch != null && batch.getUnitPrice() != null) {
                item.setUnitPrice(batch.getUnitPrice());
                item.setDiffAmount(item.getDiffQuantity() * batch.getUnitPrice());
            }

            stockTakeItemRepository.save(item);
        }

        return saved;
    }

    /**
     * 开始盘点
     */
    @Transactional(rollbackFor = Exception.class)
    public StockTake startStockTake(Long takeId, String taker) {
        StockTake stockTake = stockTakeRepository.findById(takeId)
                .orElseThrow(() -> new RuntimeException("盘点单不存在"));

        if (stockTake.getStatus() != 0) {
            throw new RuntimeException("只有新建状态的盘点单可以开始");
        }

        stockTake.setStatus(1);
        stockTake.setStartTime(LocalDateTime.now());
        stockTake.setTaker(taker);

        return stockTakeRepository.save(stockTake);
    }

    /**
     * 更新盘点明细
     */
    @Transactional(rollbackFor = Exception.class)
    public StockTakeItem updateItem(Long itemId, Integer actualQuantity, String description) {
        StockTakeItem item = stockTakeItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("盘点明细不存在"));

        item.setActualQuantity(actualQuantity);
        item.setDiffQuantity(actualQuantity - item.getBookQuantity());

        if (item.getUnitPrice() != null) {
            item.setDiffAmount(item.getDiffQuantity() * item.getUnitPrice());
        }

        // 设置状态
        if (item.getDiffQuantity() < 0) {
            item.setStatus(1); // 盘亏
        } else if (item.getDiffQuantity() > 0) {
            item.setStatus(2); // 盘盈
        } else {
            item.setStatus(0); // 正常
        }

        if (description != null) {
            item.setDescription(description);
        }

        return stockTakeItemRepository.save(item);
    }

    /**
     * 完成盘点
     */
    @Transactional(rollbackFor = Exception.class)
    public StockTake completeStockTake(Long takeId, String rechecker) {
        StockTake stockTake = stockTakeRepository.findById(takeId)
                .orElseThrow(() -> new RuntimeException("盘点单不存在"));

        if (stockTake.getStatus() != 1) {
            throw new RuntimeException("只有盘点中的盘点单可以完成");
        }

        List<StockTakeItem> items = stockTakeItemRepository.findByTakeId(takeId);

        // 统计差异
        int diffQtyTotal = 0;
        double diffAmountTotal = 0.0;

        for (StockTakeItem item : items) {
            diffQtyTotal += item.getDiffQuantity();
            if (item.getDiffAmount() != null) {
                diffAmountTotal += item.getDiffAmount();
            }
        }

        stockTake.setDiffQuantityTotal(diffQtyTotal);
        stockTake.setDiffAmountTotal(diffAmountTotal);
        stockTake.setStatus(2);
        stockTake.setCompletedAt(LocalDateTime.now());
        stockTake.setRechecker(rechecker);

        return stockTakeRepository.save(stockTake);
    }

    /**
     * 取消盘点
     */
    @Transactional(rollbackFor = Exception.class)
    public StockTake cancelStockTake(Long takeId) {
        StockTake stockTake = stockTakeRepository.findById(takeId)
                .orElseThrow(() -> new RuntimeException("盘点单不存在"));

        if (stockTake.getStatus() == 2) {
            throw new RuntimeException("已完成的盘点单不能取消");
        }

        stockTake.setStatus(3);
        return stockTakeRepository.save(stockTake);
    }

    /**
     * 查询盘点明细
     */
    public List<StockTakeItem> findItemsByTakeId(Long takeId) {
        return stockTakeItemRepository.findByTakeId(takeId);
    }

    /**
     * 检查单号是否存在
     */
    public boolean existsByOrderNo(String orderNo) {
        return stockTakeRepository.existsByOrderNo(orderNo);
    }
}
