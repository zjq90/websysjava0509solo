package com.club.repository;

import com.club.entity.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 消息模板数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long>, JpaSpecificationExecutor<MessageTemplate> {

    /**
     * 根据模板编码查询
     */
    Optional<MessageTemplate> findByCode(String code);

    /**
     * 根据模板类型查询
     */
    List<MessageTemplate> findByType(String type);

    /**
     * 查询启用的模板列表
     */
    List<MessageTemplate> findByEnabledTrue();

    /**
     * 检查模板编码是否存在
     */
    boolean existsByCode(String code);
}
