package com.heritage.repository;

import com.heritage.entity.MonitoringRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringRuleRepository extends JpaRepository<MonitoringRule, Long> {

    List<MonitoringRule> findByEnabledTrue();

    MonitoringRule findByRuleType(String ruleType);
}
