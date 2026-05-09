package com.agricultural.service;

import com.agricultural.dto.*;
import com.agricultural.entity.*;
import com.agricultural.entity.enums.*;
import com.agricultural.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 利润分析服务类
 * 从品种、客户、区域等多维度进行利润分析
 */
@Service
public class ProfitAnalysisService {

    private final OrderRepository orderRepository;
    private final VarietyRepository varietyRepository;
    private final CustomerRepository customerRepository;
    private final CostRecordRepository costRecordRepository;

    public ProfitAnalysisService(OrderRepository orderRepository,
                                 VarietyRepository varietyRepository,
                                 CustomerRepository customerRepository,
                                 CostRecordRepository costRecordRepository) {
        this.orderRepository = orderRepository;
        this.varietyRepository = varietyRepository;
        this.customerRepository = customerRepository;
        this.costRecordRepository = costRecordRepository;
    }

    /**
     * 按品种分析利润
     * 计算公式：利润 = 销售收入 - （标准成本 * 销售数量）
     */
    public List<ProfitByVarietyDTO> analyzeByVariety() {
        List<Order> salesOrders = orderRepository.findByOrderTypeOrderByOrderDateDesc(OrderType.SALES)
            .stream()
            .filter(o -> o.getOrderStatus() != OrderStatus.CANCELLED)
            .collect(Collectors.toList());
        
        Map<Long, VarietySalesData> varietyDataMap = new HashMap<>();
        
        for (Order order : salesOrders) {
            for (OrderItem item : order.getOrderItems()) {
                Long varietyId = item.getVariety().getId();
                final Variety itemVariety = item.getVariety();
                VarietySalesData data = varietyDataMap.computeIfAbsent(varietyId, 
                    k -> new VarietySalesData(itemVariety));
                data.addSales(item.getLineTotal(), item.getQuantity());
            }
        }
        
        BigDecimal totalAllProfit = varietyDataMap.values().stream()
            .map(data -> {
                BigDecimal costAmount = data.variety.getStandardCost() != null 
                    ? data.variety.getStandardCost().multiply(data.totalQuantity)
                    : BigDecimal.ZERO;
                return data.totalSales.subtract(costAmount);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<ProfitByVarietyDTO> result = new ArrayList<>();
        for (VarietySalesData data : varietyDataMap.values()) {
            BigDecimal salesCost = data.variety.getStandardCost() != null 
                ? data.variety.getStandardCost().multiply(data.totalQuantity)
                : BigDecimal.ZERO;
            BigDecimal allocatedCost = BigDecimal.ZERO;
            BigDecimal totalCost = salesCost.add(allocatedCost);
            BigDecimal profit = data.totalSales.subtract(totalCost);
            BigDecimal margin = data.totalSales.compareTo(BigDecimal.ZERO) > 0
                ? profit.multiply(new BigDecimal("100")).divide(data.totalSales, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            BigDecimal profitPercentage = totalAllProfit.compareTo(BigDecimal.ZERO) > 0
                ? profit.multiply(new BigDecimal("100")).divide(totalAllProfit, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            
            result.add(new ProfitByVarietyDTO(
                data.variety.getId(),
                data.variety.getVarietyCode(),
                data.variety.getVarietyName(),
                data.totalSales,
                salesCost,
                allocatedCost,
                totalCost,
                profit,
                margin,
                margin,
                profitPercentage
            ));
        }
        
        result.sort((a, b) -> b.getProfit().compareTo(a.getProfit()));
        return result;
    }

    /**
     * 按客户分析利润
     */
    public List<ProfitByCustomerDTO> analyzeByCustomer() {
        List<Order> allOrders = orderRepository.findAll();
        Map<Long, CustomerSalesData> customerDataMap = new HashMap<>();
        
        for (Order order : allOrders) {
            if (order.getOrderStatus() == OrderStatus.CANCELLED) continue;
            
            Long customerId = order.getCustomer().getId();
            final Customer orderCustomer = order.getCustomer();
            CustomerSalesData data = customerDataMap.computeIfAbsent(customerId,
                k -> new CustomerSalesData(orderCustomer));
            
            if (order.getOrderType() == OrderType.SALES) {
                data.addSales(order.getNetAmount());
            } else {
                data.addPurchase(order.getNetAmount());
            }
        }
        
        BigDecimal totalAllProfit = customerDataMap.values().stream()
            .map(data -> data.totalSales.subtract(data.totalPurchase))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<ProfitByCustomerDTO> result = new ArrayList<>();
        for (CustomerSalesData data : customerDataMap.values()) {
            BigDecimal profit = data.totalSales.subtract(data.totalPurchase);
            BigDecimal margin = data.totalSales.compareTo(BigDecimal.ZERO) > 0
                ? profit.multiply(new BigDecimal("100")).divide(data.totalSales, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            BigDecimal profitPercentage = totalAllProfit.compareTo(BigDecimal.ZERO) > 0
                ? profit.multiply(new BigDecimal("100")).divide(totalAllProfit, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            
            String region = data.customer.getProvince();
            if (region != null && data.customer.getCity() != null) {
                region = data.customer.getProvince() + " " + data.customer.getCity();
            }
            
            result.add(new ProfitByCustomerDTO(
                data.customer.getId(),
                data.customer.getCustomerCode(),
                data.customer.getCustomerName(),
                data.customer.getCustomerType(),
                region,
                data.totalSales,
                data.totalPurchase,
                data.totalPurchase,
                profit,
                margin,
                profitPercentage
            ));
        }
        
        result.sort((a, b) -> b.getProfit().compareTo(a.getProfit()));
        return result;
    }

    /**
     * 按区域（省份）分析利润
     */
    public List<ProfitByRegionDTO> analyzeByRegion() {
        List<Order> allOrders = orderRepository.findAll();
        Map<String, RegionSalesData> regionDataMap = new HashMap<>();
        
        Set<String> customerTypes = new HashSet<>();
        customerTypes.add("CUSTOMER");
        customerTypes.add("BOTH");
        final Set<String> finalCustomerTypes = customerTypes;
        List<Customer> allCustomers = customerRepository.findAll().stream()
            .filter(c -> finalCustomerTypes.contains(c.getCustomerType()))
            .collect(Collectors.toList());
        
        for (Order order : allOrders) {
            if (order.getOrderStatus() == OrderStatus.CANCELLED) continue;
            
            String orderProvince = order.getCustomer().getProvince();
            String orderCity = order.getCustomer().getCity();
            if (orderProvince == null) orderProvince = "未分配";
            if (orderCity == null) orderCity = "";
            final String finalProvince = orderProvince;
            final String finalCity = orderCity;
            
            String key = orderProvince + "|" + orderCity;
            RegionSalesData data = regionDataMap.computeIfAbsent(key,
                k -> new RegionSalesData(finalProvince, finalCity));
            
            if (order.getOrderType() == OrderType.SALES) {
                data.addSales(order.getNetAmount());
            } else {
                data.addPurchase(order.getNetAmount());
            }
            data.addOrder();
        }
        
        for (Customer customer : allCustomers) {
            String custProvince = customer.getProvince();
            String custCity = customer.getCity();
            if (custProvince == null) custProvince = "未分配";
            if (custCity == null) custCity = "";
            final String finalProvince = custProvince;
            final String finalCity = custCity;
            
            String key = custProvince + "|" + custCity;
            RegionSalesData data = regionDataMap.computeIfAbsent(key,
                k -> new RegionSalesData(finalProvince, finalCity));
            data.addCustomer();
        }
        
        BigDecimal totalAllProfit = regionDataMap.values().stream()
            .map(data -> data.totalSales.subtract(data.totalPurchase))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<ProfitByRegionDTO> result = new ArrayList<>();
        for (RegionSalesData data : regionDataMap.values()) {
            BigDecimal profit = data.totalSales.subtract(data.totalPurchase);
            BigDecimal margin = data.totalSales.compareTo(BigDecimal.ZERO) > 0
                ? profit.multiply(new BigDecimal("100")).divide(data.totalSales, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            BigDecimal profitPercentage = totalAllProfit.compareTo(BigDecimal.ZERO) > 0
                ? profit.multiply(new BigDecimal("100")).divide(totalAllProfit, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            
            String regionName = data.city != null && !data.city.isEmpty() 
                ? data.province + " - " + data.city 
                : data.province;
            
            result.add(new ProfitByRegionDTO(
                data.province,
                data.city,
                regionName,
                data.customerCount,
                data.totalSales,
                data.totalPurchase,
                data.totalPurchase,
                profit,
                margin,
                data.orderCount,
                profitPercentage
            ));
        }
        
        result.sort((a, b) -> b.getProfit().compareTo(a.getProfit()));
        return result;
    }

    /**
     * 获取Top N品种利润
     */
    public List<ProfitByVarietyDTO> getTopVarieties(int limit) {
        List<ProfitByVarietyDTO> all = analyzeByVariety();
        return all.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * 获取Top N客户利润
     */
    public List<ProfitByCustomerDTO> getTopCustomers(int limit) {
        List<ProfitByCustomerDTO> all = analyzeByCustomer();
        return all.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * 辅助类：品种销售数据
     */
    private static class VarietySalesData {
        Variety variety;
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;
        
        VarietySalesData(Variety variety) {
            this.variety = variety;
        }
        
        void addSales(BigDecimal amount, BigDecimal qty) {
            this.totalSales = this.totalSales.add(amount);
            this.totalQuantity = this.totalQuantity.add(qty);
        }
    }

    /**
     * 辅助类：客户销售数据
     */
    private static class CustomerSalesData {
        Customer customer;
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalPurchase = BigDecimal.ZERO;
        
        CustomerSalesData(Customer customer) {
            this.customer = customer;
        }
        
        void addSales(BigDecimal amount) {
            this.totalSales = this.totalSales.add(amount);
        }
        
        void addPurchase(BigDecimal amount) {
            this.totalPurchase = this.totalPurchase.add(amount);
        }
    }

    /**
     * 辅助类：区域销售数据
     */
    private static class RegionSalesData {
        String province;
        String city;
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalPurchase = BigDecimal.ZERO;
        int orderCount = 0;
        int customerCount = 0;
        
        RegionSalesData(String province, String city) {
            this.province = province;
            this.city = city;
        }
        
        void addSales(BigDecimal amount) {
            this.totalSales = this.totalSales.add(amount);
        }
        
        void addPurchase(BigDecimal amount) {
            this.totalPurchase = this.totalPurchase.add(amount);
        }
        
        void addOrder() {
            this.orderCount++;
        }
        
        void addCustomer() {
            this.customerCount++;
        }
    }
}
