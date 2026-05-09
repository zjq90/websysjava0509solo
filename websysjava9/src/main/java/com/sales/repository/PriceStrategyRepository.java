package com.sales.repository;

import com.sales.entity.CustomerLevel;
import com.sales.entity.CustomerType;
import com.sales.entity.PriceStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 价格策略数据访问层接口
 * 继承JpaRepository，提供基础的增删改查功能
 */
@Repository
public interface PriceStrategyRepository extends JpaRepository<PriceStrategy, Long> {

    /**
     * 查找所有启用的策略，按优先级倒序
     */
    List<PriceStrategy> findByActiveTrueOrderByPriorityDesc();

    /**
     * 按产品ID查找策略
     */
    List<PriceStrategy> findByProductId(Long productId);

    /**
     * 按客户ID查找策略
     */
    List<PriceStrategy> findByCustomerId(Long customerId);

    /**
     * 按客户类型查找策略
     */
    List<PriceStrategy> findByCustomerType(CustomerType type);

    /**
     * 按客户等级查找策略
     */
    List<PriceStrategy> findByCustomerLevel(CustomerLevel level);

    /**
     * 按区域查找策略
     */
    List<PriceStrategy> findByRegion(String region);

    /**
     * 查找启用的、指定产品的策略，按优先级排序
     */
    List<PriceStrategy> findByActiveTrueAndProductIdOrderByPriorityDesc(Long productId);

    /**
     * 分页查询所有策略，按优先级倒序
     */
    Page<PriceStrategy> findAllByOrderByPriorityDesc(Pageable pageable);

    /**
     * 分页查询所有启用的策略，按优先级倒序
     */
    Page<PriceStrategy> findByActiveTrueOrderByPriorityDesc(Pageable pageable);
}
