package com.management.service;

import com.management.entity.Purchase;
import com.management.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 采购服务类
 * 提供采购单的增删改查功能
 */
@Service
@Transactional
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private InventoryService inventoryService;

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }

    public Optional<Purchase> findById(Long id) {
        return purchaseRepository.findById(id);
    }

    public Purchase save(Purchase purchase) {
        if (purchase.getPurchaseNo() == null) {
            purchase.setPurchaseNo("CG" + System.currentTimeMillis());
        }
        if (purchase.getUnitPrice() != null && purchase.getQuantity() != null) {
            purchase.setTotalAmount(purchase.getUnitPrice().multiply(BigDecimal.valueOf(purchase.getQuantity())));
        }

        Purchase savedPurchase = purchaseRepository.save(purchase);

        if (purchase.getProduct() != null && "已入库".equals(purchase.getStatus())) {
            inventoryService.updateStockIn(
                purchase.getProduct().getId(),
                purchase.getQuantity(),
                purchase.getUnitPrice(),
                purchase.getProduct()
            );
        }

        return savedPurchase;
    }

    public void deleteById(Long id) {
        purchaseRepository.deleteById(id);
    }

    public List<Purchase> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return purchaseRepository.findByPurchaseDateBetween(startDate, endDate);
    }

    public BigDecimal getTotalPurchaseAmount(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal value = purchaseRepository.sumTotalAmountByDateRange(startDate, endDate);
        return value != null ? value : BigDecimal.ZERO;
    }

    public Integer getPurchaseQuantity(Long productId, LocalDateTime startDate, LocalDateTime endDate) {
        Integer value = purchaseRepository.sumQuantityByProductAndDateRange(productId, startDate, endDate);
        return value != null ? value : 0;
    }
}
