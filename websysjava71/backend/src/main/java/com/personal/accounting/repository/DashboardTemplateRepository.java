package com.personal.accounting.repository;

import com.personal.accounting.entity.DashboardTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 看板模板数据访问接口
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Repository
public interface DashboardTemplateRepository extends JpaRepository<DashboardTemplate, Long> {

    /**
     * 查询默认模板
     */
    Optional<DashboardTemplate> findByIsDefaultTrue();
}
