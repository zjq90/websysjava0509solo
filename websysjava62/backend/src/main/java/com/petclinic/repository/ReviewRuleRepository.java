package com.petclinic.repository;

import com.petclinic.entity.ReviewRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 审核规则Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface ReviewRuleRepository extends JpaRepository<ReviewRule, Long> {

    /**
     * 根据规则类型查询
     */
    List<ReviewRule> findByRuleType(String ruleType);

    /**
     * 查询启用的规则
     */
    List<ReviewRule> findByEnabled(Boolean enabled);

    /**
     * 根据规则名称查询
     */
    List<ReviewRule> findByRuleNameContaining(String ruleName);
}
