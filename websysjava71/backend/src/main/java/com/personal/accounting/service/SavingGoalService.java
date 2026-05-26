package com.personal.accounting.service;

import com.personal.accounting.entity.SavingGoal;
import com.personal.accounting.repository.SavingGoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 储蓄目标服务类
 * 提供储蓄目标的管理和进度跟踪功能
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SavingGoalService {

    private final SavingGoalRepository savingGoalRepository;

    /**
     * 获取所有储蓄目标
     */
    @Transactional(readOnly = true)
    public List<SavingGoal> findAll() {
        return savingGoalRepository.findAll();
    }

    /**
     * 获取进行中的储蓄目标
     */
    @Transactional(readOnly = true)
    public List<SavingGoal> findActiveGoals() {
        return savingGoalRepository.findByCompletedFalseOrderByCreatedAtDesc();
    }

    /**
     * 获取已完成的储蓄目标
     */
    @Transactional(readOnly = true)
    public List<SavingGoal> findCompletedGoals() {
        return savingGoalRepository.findByCompletedTrueOrderByCompletedAtDesc();
    }

    /**
     * 根据ID查询储蓄目标
     */
    @Transactional(readOnly = true)
    public SavingGoal findById(Long id) {
        return savingGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("储蓄目标不存在: " + id));
    }

    /**
     * 创建储蓄目标
     */
    @Transactional
    public SavingGoal create(SavingGoal goal) {
        log.debug("创建储蓄目标: {}", goal.getName());
        goal.setCompleted(false);
        goal.setCurrentAmount(BigDecimal.ZERO);
        SavingGoal saved = savingGoalRepository.save(goal);
        log.info("储蓄目标创建成功: {}", saved.getId());
        return saved;
    }

    /**
     * 更新储蓄目标
     */
    @Transactional
    public SavingGoal update(Long id, SavingGoal goal) {
        log.debug("更新储蓄目标: {}", id);
        
        SavingGoal existing = savingGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("储蓄目标不存在: " + id));
        
        existing.setName(goal.getName());
        existing.setDescription(goal.getDescription());
        existing.setTargetAmount(goal.getTargetAmount());
        existing.setTargetDate(goal.getTargetDate());
        existing.setColor(goal.getColor());
        existing.setIcon(goal.getIcon());
        
        // 检查是否完成
        checkCompletion(existing);
        
        SavingGoal saved = savingGoalRepository.save(existing);
        log.info("储蓄目标更新成功: {}", saved.getId());
        return saved;
    }

    /**
     * 删除储蓄目标
     */
    @Transactional
    public void delete(Long id) {
        log.debug("删除储蓄目标: {}", id);
        savingGoalRepository.deleteById(id);
        log.info("储蓄目标已删除: {}", id);
    }

    /**
     * 向储蓄目标存款
     */
    @Transactional
    public SavingGoal deposit(Long id, BigDecimal amount) {
        log.debug("向储蓄目标 {} 存款: {}", id, amount);
        
        SavingGoal goal = savingGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("储蓄目标不存在: " + id));
        
        goal.setCurrentAmount(goal.getCurrentAmount().add(amount));
        
        // 检查是否完成
        checkCompletion(goal);
        
        SavingGoal saved = savingGoalRepository.save(goal);
        log.info("存款成功，当前进度: {} / {}", saved.getCurrentAmount(), saved.getTargetAmount());
        return saved;
    }

    /**
     * 从储蓄目标取款
     */
    @Transactional
    public SavingGoal withdraw(Long id, BigDecimal amount) {
        log.debug("从储蓄目标 {} 取款: {}", id, amount);
        
        SavingGoal goal = savingGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("储蓄目标不存在: " + id));
        
        if (goal.getCurrentAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException("余额不足");
        }
        
        goal.setCurrentAmount(goal.getCurrentAmount().subtract(amount));
        goal.setCompleted(false);
        goal.setCompletedAt(null);
        
        SavingGoal saved = savingGoalRepository.save(goal);
        log.info("取款成功，当前余额: {}", saved.getCurrentAmount());
        return saved;
    }

    /**
     * 检查目标是否完成
     */
    private void checkCompletion(SavingGoal goal) {
        if (!goal.getCompleted() && goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            goal.setCompleted(true);
            goal.setCompletedAt(LocalDateTime.now());
            log.info("储蓄目标已完成: {}", goal.getName());
        }
    }

    /**
     * 获取储蓄进度百分比
     */
    @Transactional(readOnly = true)
    public double getProgressPercentage(Long id) {
        SavingGoal goal = findById(id);
        if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }
        return goal.getCurrentAmount().doubleValue() / goal.getTargetAmount().doubleValue() * 100;
    }
}
