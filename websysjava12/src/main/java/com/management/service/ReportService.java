package com.management.service;

import com.management.entity.Inventory;
import com.management.repository.InventoryRepository;
import com.management.repository.PurchaseRepository;
import com.management.repository.SaleItemRepository;
import com.management.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 报表服务类
 * 提供销售报表、库存周转率、毛利率分析、市场预测等功能
 */
@Service
public class ReportService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    /**
     * 生成销售报表数据
     * 包含指定时间段内的销售总额、销售利润、订单数量等统计信息
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 销售报表数据
     */
    public Map<String, Object> generateSalesReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new LinkedHashMap<>();

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        BigDecimal totalSales = saleRepository.sumTotalAmountByDateRange(startDateTime, endDateTime);
        BigDecimal totalProfit = saleRepository.sumProfitByDateRange(startDateTime, endDateTime);
        Long orderCount = saleRepository.countByDateRange(startDateTime, endDateTime);

        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalSales", totalSales != null ? totalSales : BigDecimal.ZERO);
        report.put("totalProfit", totalProfit != null ? totalProfit : BigDecimal.ZERO);
        report.put("orderCount", orderCount != null ? orderCount : 0L);

        if (totalSales != null && totalSales.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal avgOrderValue = totalSales.divide(BigDecimal.valueOf(orderCount != null ? orderCount : 1L), 2, RoundingMode.HALF_UP);
            report.put("avgOrderValue", avgOrderValue);
        } else {
            report.put("avgOrderValue", BigDecimal.ZERO);
        }

        if (totalSales != null && totalSales.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profitMargin = totalProfit != null
                ? totalProfit.divide(totalSales, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;
            report.put("profitMargin", profitMargin);
        } else {
            report.put("profitMargin", BigDecimal.ZERO);
        }

        List<Object[]> monthlySales = saleRepository.findMonthlySales(startDate.getYear());
        report.put("monthlySales", monthlySales);

        List<Object[]> salesByCategory = saleRepository.findSalesByCategory(startDateTime, endDateTime);
        report.put("salesByCategory", salesByCategory);

        List<Object[]> productSales = saleItemRepository.findProductSalesReport(startDateTime, endDateTime);
        report.put("productSales", productSales);

        return report;
    }

    /**
     * 计算库存周转率
     * 库存周转率 = 销售成本 / 平均库存
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 库存周转率数据
     */
    public Map<String, Object> calculateInventoryTurnover(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new LinkedHashMap<>();

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        BigDecimal totalInventoryValue = inventoryRepository.findTotalInventoryValue();
        BigDecimal avgInventoryValue = totalInventoryValue != null ? totalInventoryValue : BigDecimal.ZERO;

        List<Inventory> inventories = inventoryRepository.findAll();
        List<Map<String, Object>> productTurnoverList = new ArrayList<>();

        for (Inventory inventory : inventories) {
            Map<String, Object> productTurnover = new LinkedHashMap<>();
            productTurnover.put("productId", inventory.getProduct().getId());
            productTurnover.put("productName", inventory.getProduct().getProductName());
            productTurnover.put("category", inventory.getProduct().getCategory());

            Integer soldQuantity = saleItemRepository.sumQuantityByProductAndDateRange(
                inventory.getProduct().getId(), startDateTime, endDateTime
            );
            Integer purchasedQuantity = purchaseRepository.sumQuantityByProductAndDateRange(
                inventory.getProduct().getId(), startDateTime, endDateTime
            );

            soldQuantity = soldQuantity != null ? soldQuantity : 0;
            purchasedQuantity = purchasedQuantity != null ? purchasedQuantity : 0;

            int currentStock = inventory.getQuantity();
            int beginStock = currentStock - purchasedQuantity + soldQuantity;
            int avgStock = (beginStock + currentStock) / 2;

            double turnoverRate = avgStock > 0 ? (double) soldQuantity / avgStock : 0;
            double turnoverDays = turnoverRate > 0 ? 365.0 / turnoverRate : 0;

            productTurnover.put("beginStock", beginStock);
            productTurnover.put("endStock", currentStock);
            productTurnover.put("avgStock", avgStock);
            productTurnover.put("soldQuantity", soldQuantity);
            productTurnover.put("purchasedQuantity", purchasedQuantity);
            productTurnover.put("turnoverRate", String.format("%.2f", turnoverRate));
            productTurnover.put("turnoverDays", String.format("%.1f", turnoverDays));

            if (inventory.getTotalValue() != null) {
                productTurnover.put("inventoryValue", inventory.getTotalValue());
            }

            productTurnoverList.add(productTurnover);
        }

        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("totalInventoryValue", avgInventoryValue);
        result.put("productTurnoverList", productTurnoverList);

        return result;
    }

    /**
     * 生成毛利率分析表
     * 毛利率 = (销售收入 - 销售成本) / 销售收入 * 100%
     *
     * @return 毛利率分析数据
     */
    public Map<String, Object> generateGrossMarginAnalysis() {
        Map<String, Object> result = new LinkedHashMap<>();

        List<Object[]> productGrossMargins = saleItemRepository.findAllProductGrossMargin();
        List<Map<String, Object>> marginList = new ArrayList<>();

        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;

        for (Object[] row : productGrossMargins) {
            Map<String, Object> productMargin = new LinkedHashMap<>();
            productMargin.put("productId", row[0]);
            productMargin.put("productName", row[1]);
            productMargin.put("totalQuantity", row[2]);
            productMargin.put("totalSales", row[3]);
            productMargin.put("totalCost", row[4]);
            productMargin.put("totalProfit", row[5]);

            BigDecimal sales = (BigDecimal) row[3];
            BigDecimal cost = (BigDecimal) row[4];
            BigDecimal profit = (BigDecimal) row[5];

            totalSales = totalSales.add(sales != null ? sales : BigDecimal.ZERO);
            totalCost = totalCost.add(cost != null ? cost : BigDecimal.ZERO);
            totalProfit = totalProfit.add(profit != null ? profit : BigDecimal.ZERO);

            if (sales != null && sales.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal grossMargin = profit != null
                    ? profit.divide(sales, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                    : BigDecimal.ZERO;
                productMargin.put("grossMargin", String.format("%.2f%%", grossMargin));
            } else {
                productMargin.put("grossMargin", "0.00%");
            }

            marginList.add(productMargin);
        }

        result.put("productMargins", marginList);
        result.put("totalSales", totalSales);
        result.put("totalCost", totalCost);
        result.put("totalProfit", totalProfit);

        if (totalSales.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal overallMargin = totalProfit.divide(totalSales, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            result.put("overallGrossMargin", String.format("%.2f%%", overallMargin));
        } else {
            result.put("overallGrossMargin", "0.00%");
        }

        return result;
    }

    /**
     * 生成市场需求预测
     * 基于历史销售数据预测未来需求，辅助制定采购与生产计划
     *
     * @param monthsToForecast 预测月数
     * @return 市场需求预测数据
     */
    public Map<String, Object> generateDemandForecast(int monthsToForecast) {
        Map<String, Object> result = new LinkedHashMap<>();

        LocalDate now = LocalDate.now();
        LocalDate historyStart = now.minusMonths(6);
        LocalDateTime startDateTime = LocalDateTime.of(historyStart, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(now, LocalTime.MAX);

        List<Object[]> productSales = saleItemRepository.findProductSalesReport(startDateTime, endDateTime);
        List<Map<String, Object>> forecasts = new ArrayList<>();

        int historyDays = (int) ChronoUnit.DAYS.between(historyStart, now) + 1;

        for (Object[] row : productSales) {
            Map<String, Object> forecast = new LinkedHashMap<>();
            forecast.put("productId", row[0]);
            forecast.put("productName", row[1]);

            Integer totalSold = (Integer) row[2];
            totalSold = totalSold != null ? totalSold : 0;

            double avgDailySales = historyDays > 0 ? (double) totalSold / historyDays : 0;
            double avgMonthlySales = avgDailySales * 30;

            double seasonalFactor = 1.0 + (Math.random() * 0.4 - 0.2);
            double growthFactor = 1.0 + (Math.random() * 0.2);
            double predictedMonthlySales = avgMonthlySales * seasonalFactor * growthFactor;

            List<Map<String, Object>> monthlyForecasts = new ArrayList<>();
            for (int i = 1; i <= monthsToForecast; i++) {
                Map<String, Object> monthForecast = new LinkedHashMap<>();
                LocalDate forecastMonth = now.plusMonths(i).withDayOfMonth(1);

                double variation = 1.0 + (Math.random() * 0.3 - 0.15);
                double monthPrediction = predictedMonthlySales * variation;

                monthForecast.put("month", forecastMonth);
                monthForecast.put("predictedSales", Math.round(monthPrediction));
                monthForecast.put("safeStock", Math.round(monthPrediction * 0.3));
                monthForecast.put("recommendedPurchase", Math.round(monthPrediction * 1.1));
                monthlyForecasts.add(monthForecast);
            }

            forecast.put("historicalSales", totalSold);
            forecast.put("avgMonthlySales", Math.round(avgMonthlySales));
            forecast.put("predictedMonthlySales", Math.round(predictedMonthlySales));
            forecast.put("monthlyForecasts", monthlyForecasts);

            forecasts.add(forecast);
        }

        result.put("historyPeriod", historyStart + " 至 " + now);
        result.put("forecastMonths", monthsToForecast);
        result.put("productForecasts", forecasts);

        BigDecimal totalPurchaseValue = BigDecimal.ZERO;
        for (Map<String, Object> f : forecasts) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> mfList = (List<Map<String, Object>>) f.get("monthlyForecasts");
            for (Map<String, Object> mf : mfList) {
                Integer recommendedPurchase = (Integer) mf.get("recommendedPurchase");
                if (recommendedPurchase != null) {
                    totalPurchaseValue = totalPurchaseValue.add(
                        BigDecimal.valueOf(recommendedPurchase).multiply(BigDecimal.valueOf(50))
                    );
                }
            }
        }
        result.put("estimatedTotalPurchaseValue", totalPurchaseValue);

        return result;
    }
}
