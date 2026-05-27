package com.club.service;

import com.club.dto.PageQuery;
import com.club.entity.MessageTemplate;
import com.club.entity.DataBackup;
import com.club.entity.User;
import com.club.enums.RoleType;
import com.club.repository.MessageTemplateRepository;
import com.club.repository.DataBackupRepository;
import com.club.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置服务类
 * 包含权限管理、消息模板、数据备份等业务逻辑
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Service
public class SystemService {

    private final UserRepository userRepository;
    private final MessageTemplateRepository messageTemplateRepository;
    private final DataBackupRepository dataBackupRepository;

    public SystemService(UserRepository userRepository,
                        MessageTemplateRepository messageTemplateRepository,
                        DataBackupRepository dataBackupRepository) {
        this.userRepository = userRepository;
        this.messageTemplateRepository = messageTemplateRepository;
        this.dataBackupRepository = dataBackupRepository;
    }

    // ==================== 用户/权限管理 ====================

    /**
     * 分页查询用户列表
     */
    public Page<User> getUserList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<User> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("username"), "%" + query.getKeyword() + "%"),
                    criteriaBuilder.like(root.get("realName"), "%" + query.getKeyword() + "%")
                ));
            }
            if (StringUtils.hasText(query.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("roleType"), query.getType()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return userRepository.findAll(spec, pageable);
    }

    /**
     * 根据ID查询用户
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * 创建用户
     */
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        return userRepository.save(user);
    }

    /**
     * 更新用户
     */
    public User updateUser(Long id, User user) {
        User existing = getUserById(id);
        if (existing == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 分配用户角色
     */
    public User assignRole(Long userId, String role, Long clubId) {
        User user = getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setRoleType(RoleType.valueOf(role));
        user.setClubId(clubId);
        return userRepository.save(user);
    }

    // ==================== 消息模板管理 ====================

    /**
     * 分页查询消息模板列表
     */
    public Page<MessageTemplate> getMessageTemplateList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<MessageTemplate> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), "%" + query.getKeyword() + "%"),
                    criteriaBuilder.like(root.get("code"), "%" + query.getKeyword() + "%")
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return messageTemplateRepository.findAll(spec, pageable);
    }

    /**
     * 根据ID查询消息模板
     */
    public MessageTemplate getMessageTemplateById(Long id) {
        return messageTemplateRepository.findById(id).orElse(null);
    }

    /**
     * 创建消息模板
     */
    public MessageTemplate createMessageTemplate(MessageTemplate template) {
        if (messageTemplateRepository.existsByCode(template.getCode())) {
            throw new RuntimeException("模板编码已存在");
        }
        return messageTemplateRepository.save(template);
    }

    /**
     * 更新消息模板
     */
    public MessageTemplate updateMessageTemplate(Long id, MessageTemplate template) {
        MessageTemplate existing = getMessageTemplateById(id);
        if (existing == null) {
            throw new RuntimeException("模板不存在");
        }
        template.setId(id);
        return messageTemplateRepository.save(template);
    }

    /**
     * 删除消息模板
     */
    public void deleteMessageTemplate(Long id) {
        messageTemplateRepository.deleteById(id);
    }

    // ==================== 数据备份管理 ====================

    /**
     * 分页查询备份记录列表
     */
    public Page<DataBackup> getDataBackupList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<DataBackup> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + query.getKeyword() + "%"));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return dataBackupRepository.findAll(spec, pageable);
    }

    /**
     * 创建数据备份
     */
    public DataBackup createBackup(String name, String description, Long operatorId, String operatorName) {
        DataBackup backup = new DataBackup();
        backup.setName(name);
        backup.setType("MANUAL");
        backup.setDescription(description);
        backup.setStatus(0);
        backup.setStartTime(LocalDateTime.now());
        backup.setOperatorId(operatorId);
        backup.setOperatorName(operatorName);
        
        DataBackup saved = dataBackupRepository.save(backup);
        
        // 模拟备份过程
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                saved.setStatus(1);
                saved.setEndTime(LocalDateTime.now());
                saved.setFilePath("/backup/" + System.currentTimeMillis() + "_backup.sql");
                saved.setFileSize(1024L * 1024);
                dataBackupRepository.save(saved);
            } catch (InterruptedException e) {
                saved.setStatus(2);
                saved.setErrorMessage("备份失败：" + e.getMessage());
                dataBackupRepository.save(saved);
            }
        }).start();
        
        return saved;
    }

    /**
     * 恢复数据备份
     */
    public boolean restoreBackup(Long id) {
        DataBackup backup = dataBackupRepository.findById(id).orElse(null);
        if (backup == null || backup.getStatus() != 1) {
            return false;
        }
        // 模拟恢复过程
        return true;
    }

    /**
     * 删除备份记录
     */
    public void deleteBackup(Long id) {
        dataBackupRepository.deleteById(id);
    }

    /**
     * 获取系统概览统计
     */
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalUsers", userRepository.count());
        overview.put("totalTemplates", messageTemplateRepository.count());
        overview.put("enabledTemplates", messageTemplateRepository.findByEnabledTrue().size());
        overview.put("recentBackups", dataBackupRepository.findTop10ByStatusOrderByCreateTimeDesc(1));
        return overview;
    }
}
