package com.personal.accounting.repository;

import com.personal.accounting.entity.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 储蓄目标数据访问接口
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, Long> {

    /**
     * 查询未完成的储蓄目标
     */
    List<SavingGoal> findByCompletedFalseOrderByCreatedAtDesc();

    /**
     * 查询已完成的储蓄目标
     */
    List<SavingGoal> findByCompletedTrueOrderByCompletedAtDesc();
}
