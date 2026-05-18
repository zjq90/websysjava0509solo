package com.heritage.repository;

import com.heritage.entity.DesensitizationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesensitizationRuleRepository extends JpaRepository<DesensitizationRule, Long> {

    List<DesensitizationRule> findByEnabledTrueOrderBySortOrderAsc();

    DesensitizationRule findByFieldName(String fieldName);
}
