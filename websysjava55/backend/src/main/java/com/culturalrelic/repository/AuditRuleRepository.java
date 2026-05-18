package com.culturalrelic.repository;

import com.culturalrelic.entity.AuditRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 审核规则数据访问层
 */
@Repository
public interface AuditRuleRepository extends JpaRepository<AuditRule, Long>, JpaSpecificationExecutor<AuditRule> {

    /**
     * 根据规则编号查询
     */
    AuditRule findByRuleNo(String ruleNo);

    /**
     * 根据规则类型查询
     */
    List<AuditRule> findByRuleType(Integer ruleType);

    /**
     * 根据状态查询
     */
    List<AuditRule> findByStatus(Integer status);

    /**
     * 逻辑删除
     */
    @Modifying
    @Query("UPDATE AuditRule r SET r.deleted = 1 WHERE r.id = :id")
    int logicDelete(@Param("id") Long id);
}
