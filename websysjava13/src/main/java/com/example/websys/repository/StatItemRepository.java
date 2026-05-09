package com.example.websys.repository;

import com.example.websys.entity.StatItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据统计项数据访问接口
 * 提供统计项的增删改查功能
 */
@Repository
public interface StatItemRepository extends JpaRepository<StatItem, Long> {

    List<StatItem> findByStatusOrderBySortOrderAsc(Integer status);

    List<StatItem> findByIsDefaultTrueAndStatusOrderBySortOrderAsc(Integer status);

    StatItem findByItemCode(String itemCode);
}
