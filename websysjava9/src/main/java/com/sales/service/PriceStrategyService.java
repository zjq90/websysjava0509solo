package com.sales.service;

import com.sales.entity.*;
import com.sales.repository.PriceStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 价格策略服务层
 * 处理销售价格策略管理，支持不同客户不同报价、区域定价
 */
@Service
@Transactional
public class PriceStrategyService {

    @Autowired
    private PriceStrategyRepository priceStrategyRepository;

    /**
     * 获取所有策略
     */
    public List<PriceStrategy> findAll() {
        return priceStrategyRepository.findAll();
    }

    /**
     * 获取所有启用的策略，按优先级排序
     */
    public List<PriceStrategy> findAllActive() {
        return priceStrategyRepository.findByActiveTrueOrderByPriorityDesc();
    }

    /**
     * 根据ID获取策略
     */
    public Optional<PriceStrategy> findById(Long id) {
        return priceStrategyRepository.findById(id);
    }

    /**
     * 保存策略
     */
    public PriceStrategy save(PriceStrategy strategy) {
        return priceStrategyRepository.save(strategy);
    }

    /**
     * 更新策略
     */
    public PriceStrategy update(Long id, PriceStrategy strategyDetails) {
        return priceStrategyRepository.findById(id).map(strategy -> {
            strategy.setName(strategyDetails.getName());
            strategy.setProduct(strategyDetails.getProduct());
            strategy.setCustomer(strategyDetails.getCustomer());
            strategy.setCustomerType(strategyDetails.getCustomerType());
            strategy.setCustomerLevel(strategyDetails.getCustomerLevel());
            strategy.setRegion(strategyDetails.getRegion());
            strategy.setPriceType(strategyDetails.getPriceType());
            strategy.setDiscountRate(strategyDetails.getDiscountRate());
            strategy.setFixedPrice(strategyDetails.getFixedPrice());
            strategy.setMinQuantity(strategyDetails.getMinQuantity());
            strategy.setActive(strategyDetails.getActive());
            strategy.setPriority(strategyDetails.getPriority());
            strategy.setDescription(strategyDetails.getDescription());
            return priceStrategyRepository.save(strategy);
        }).orElseThrow(() -> new RuntimeException("价格策略不存在: " + id));
    }

    /**
     * 删除策略
     */
    public void delete(Long id) {
        priceStrategyRepository.deleteById(id);
    }

    /**
     * 计算特定客户购买特定产品的单价
     * 匹配优先级：指定客户 > 指定区域 + 客户等级 > 客户类型 > 默认价格
     */
    public BigDecimal calculatePrice(Product product, Customer customer, Integer quantity) {
        // 获取所有启用的策略
        List<PriceStrategy> allStrategies = priceStrategyRepository.findByActiveTrueOrderByPriorityDesc();

        // 按优先级匹配最适合的策略
        Optional<PriceStrategy> bestMatch = allStrategies.stream()
            .filter(s -> isStrategyApplicable(s, product, customer, quantity))
            .max(Comparator.comparingInt(PriceStrategy::getPriority));

        if (bestMatch.isPresent()) {
            PriceStrategy strategy = bestMatch.get();
            return applyStrategy(product, strategy);
        }

        // 没有匹配的策略，返回基础价格
        return product.getBasePrice();
    }

    /**
     * 检查策略是否适用
     */
    private boolean isStrategyApplicable(PriceStrategy strategy, Product product, Customer customer, Integer quantity) {
        // 检查产品匹配（策略未指定产品则适用于所有产品）
        if (strategy.getProduct() != null && !strategy.getProduct().getId().equals(product.getId())) {
            return false;
        }

        // 检查数量限制
        if (strategy.getMinQuantity() != null && quantity != null && quantity < strategy.getMinQuantity()) {
            return false;
        }

        // 检查客户匹配优先级
        boolean customerMatch = false;

        // 1. 指定客户匹配（最高优先级）
        if (strategy.getCustomer() != null) {
            customerMatch = strategy.getCustomer().getId().equals(customer.getId());
            if (!customerMatch) return false;
            customerMatch = true;
        }

        // 2. 区域匹配
        if (strategy.getRegion() != null && !strategy.getRegion().isEmpty()) {
            if (customer.getProvince() == null || !customer.getProvince().equals(strategy.getRegion())) {
                return false;
            }
            customerMatch = true;
        }

        // 3. 客户等级匹配
        if (strategy.getCustomerLevel() != null) {
            if (!strategy.getCustomerLevel().equals(customer.getLevel())) {
                return false;
            }
            customerMatch = true;
        }

        // 4. 客户类型匹配
        if (strategy.getCustomerType() != null) {
            if (!strategy.getCustomerType().equals(customer.getType())) {
                return false;
            }
            customerMatch = true;
        }

        // 如果策略没有指定任何客户相关条件，则为通用策略，适用
        return customerMatch || 
            (strategy.getCustomer() == null && strategy.getRegion() == null && 
             strategy.getCustomerLevel() == null && strategy.getCustomerType() == null);
    }

    /**
     * 应用策略计算价格
     */
    private BigDecimal applyStrategy(Product product, PriceStrategy strategy) {
        if (strategy.getPriceType() == PriceType.FIXED) {
            return strategy.getFixedPrice();
        } else {
            // 折扣定价
            return product.getBasePrice().multiply(strategy.getDiscountRate());
        }
    }

    /**
     * 分页查询所有策略
     */
    public Page<PriceStrategy> findAll(Pageable pageable) {
        return priceStrategyRepository.findAllByOrderByPriorityDesc(pageable);
    }

    /**
     * 分页查询所有启用的策略
     */
    public Page<PriceStrategy> findAllActive(Pageable pageable) {
        return priceStrategyRepository.findByActiveTrueOrderByPriorityDesc(pageable);
    }
}
