package com.management.service;

import com.management.entity.Product;
import com.management.entity.Sale;
import com.management.entity.SaleItem;
import com.management.repository.SaleItemRepository;
import com.management.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 销售服务类
 * 提供销售单的增删改查功能
 */
@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private InventoryService inventoryService;

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale save(Sale sale) {
        if (sale.getSaleNo() == null) {
            sale.setSaleNo("XS" + System.currentTimeMillis());
        }
        calculateSaleTotal(sale);
        Sale savedSale = saleRepository.save(sale);

        if (sale.getSaleItems() != null) {
            for (SaleItem item : sale.getSaleItems()) {
                item.setSale(savedSale);
                if (item.getUnitPrice() == null && item.getProduct() != null) {
                    item.setUnitPrice(item.getProduct().getSalePrice());
                }
                if (item.getCostPrice() == null && item.getProduct() != null) {
                    item.setCostPrice(item.getProduct().getCostPrice());
                }
                if (item.getQuantity() != null && item.getUnitPrice() != null) {
                    item.setAmount(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }
                if (item.getQuantity() != null && item.getCostPrice() != null) {
                    item.setCost(item.getCostPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }
                if (item.getAmount() != null && item.getCost() != null) {
                    item.setProfit(item.getAmount().subtract(item.getCost()));
                }
                saleItemRepository.save(item);

                if (item.getProduct() != null) {
                    inventoryService.updateStockOut(item.getProduct().getId(), item.getQuantity());
                }
            }
        }

        calculateSaleTotal(savedSale);
        return saleRepository.save(savedSale);
    }

    private void calculateSaleTotal(Sale sale) {
        if (sale.getSaleItems() != null && !sale.getSaleItems().isEmpty()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalCost = BigDecimal.ZERO;
            for (SaleItem item : sale.getSaleItems()) {
                if (item.getAmount() != null) {
                    totalAmount = totalAmount.add(item.getAmount());
                }
                if (item.getCost() != null) {
                    totalCost = totalCost.add(item.getCost());
                }
            }
            sale.setTotalAmount(totalAmount);
            sale.setTotalCost(totalCost);
            sale.setProfit(totalAmount.subtract(totalCost));
        }
    }

    public void deleteById(Long id) {
        saleRepository.deleteById(id);
    }

    public List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate);
    }

    public BigDecimal getTotalSalesAmount(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal value = saleRepository.sumTotalAmountByDateRange(startDate, endDate);
        return value != null ? value : BigDecimal.ZERO;
    }

    public BigDecimal getTotalProfit(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal value = saleRepository.sumProfitByDateRange(startDate, endDate);
        return value != null ? value : BigDecimal.ZERO;
    }

    public Long getSaleCount(LocalDateTime startDate, LocalDateTime endDate) {
        Long count = saleRepository.countByDateRange(startDate, endDate);
        return count != null ? count : 0L;
    }

    public List<Object[]> getMonthlySales(int year) {
        return saleRepository.findMonthlySales(year);
    }

    public List<Object[]> getSalesByCategory(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findSalesByCategory(startDate, endDate);
    }

    public List<Object[]> getProductSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        return saleItemRepository.findProductSalesReport(startDate, endDate);
    }

    public List<Object[]> getAllProductGrossMargin() {
        return saleItemRepository.findAllProductGrossMargin();
    }

    public BigDecimal getTodaySales() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return getTotalSalesAmount(todayStart, todayEnd);
    }

    public BigDecimal getMonthSales() {
        LocalDate now = LocalDate.now();
        LocalDateTime monthStart = LocalDateTime.of(now.withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime monthEnd = LocalDateTime.of(now.withDayOfMonth(now.lengthOfMonth()), LocalTime.MAX);
        return getTotalSalesAmount(monthStart, monthEnd);
    }
}
