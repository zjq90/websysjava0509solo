package com.websys.service;

import com.websys.common.PageResult;
import com.websys.entity.Agent;
import com.websys.entity.OperationLog;
import com.websys.entity.User;
import com.websys.repository.AgentRepository;
import com.websys.repository.OperationLogRepository;
import com.websys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 操作日志服务类
 * 
 * @author websys
 * @version 1.0.0
 */
@Service
public class OperationLogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgentRepository agentRepository;

    /**
     * 分页查询操作日志（支持数据隔离）
     * 
     * @param username 操作用户名
     * @param module 操作模块
     * @param operationType 操作类型
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param current 当前页
     * @param size 每页大小
     * @param roleType 角色类型（用于数据隔离）
     * @param userAgentId 用户所属代理商ID（用于数据隔离）
     * @return 分页结果
     */
    public PageResult<OperationLog> getLogPageWithPermission(String username, String module, String operationType,
                                                              Integer status, LocalDateTime startTime, LocalDateTime endTime,
                                                              Integer current, Integer size,
                                                              String roleType, Long userAgentId) {
        Pageable pageable = PageRequest.of(current - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<OperationLog> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(username)) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }

            if (StringUtils.hasText(module)) {
                predicates.add(criteriaBuilder.equal(root.get("module"), module));
            }

            if (StringUtils.hasText(operationType)) {
                predicates.add(criteriaBuilder.equal(root.get("operationType"), operationType));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (startTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), startTime));
            }

            if (endTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), endTime));
            }

            // 数据隔离：非超级管理员只能看到自己和下级代理商用户的操作日志
            if (!"ADMIN".equals(roleType) && userAgentId != null) {
                List<Long> accessibleUserIds = getAccessibleUserIds(userAgentId);
                if (!accessibleUserIds.isEmpty()) {
                    predicates.add(root.get("userId").in(accessibleUserIds));
                } else {
                    // 如果没有可访问的用户ID，设置一个永远为false的条件
                    predicates.add(criteriaBuilder.equal(root.get("id"), -1L));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<OperationLog> page = operationLogRepository.findAll(spec, pageable);
        return PageResult.of(page.getContent(), page.getTotalElements(), current, size);
    }

    /**
     * 获取用户可访问的所有用户ID列表
     * 用于操作日志的数据隔离
     * 
     * @param userAgentId 用户所属代理商ID
     * @return 可访问的用户ID列表
     */
    private List<Long> getAccessibleUserIds(Long userAgentId) {
        Set<Long> userIds = new HashSet<>();
        
        // 获取当前代理商及其所有下级代理商的ID
        List<Long> agentIds = getAllDescendantAgentIds(userAgentId);
        
        // 遍历所有代理商，获取其下的用户ID
        for (Long agentId : agentIds) {
            List<User> users = userRepository.findByAgentId(agentId);
            userIds.addAll(users.stream().map(User::getId).collect(Collectors.toList()));
        }
        
        return new ArrayList<>(userIds);
    }

    /**
     * 获取代理商及其所有下级代理商的ID列表
     * 直接使用AgentRepository，避免与AgentService的循环依赖
     * 
     * @param agentId 代理商ID
     * @return ID列表
     */
    private List<Long> getAllDescendantAgentIds(Long agentId) {
        List<Long> ids = new ArrayList<>();
        collectDescendantAgentIds(agentId, ids);
        return ids;
    }

    /**
     * 递归收集所有下级代理商ID
     * 
     * @param agentId 代理商ID
     * @param ids ID列表
     */
    private void collectDescendantAgentIds(Long agentId, List<Long> ids) {
        ids.add(agentId);
        List<Agent> children = agentRepository.findByParentId(agentId);
        for (Agent child : children) {
            collectDescendantAgentIds(child.getId(), ids);
        }
    }

    /**
     * 根据ID查询日志详情（支持数据隔离校验）
     * 
     * @param id 日志ID
     * @param roleType 角色类型
     * @param userAgentId 用户所属代理商ID
     * @return 日志详情
     */
    public OperationLog getLogByIdWithPermission(Long id, String roleType, Long userAgentId) {
        OperationLog log = operationLogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("日志不存在"));
        
        // 数据隔离校验：非超级管理员只能访问自己有权限的日志
        if (!"ADMIN".equals(roleType) && userAgentId != null && log.getUserId() != null) {
            List<Long> accessibleUserIds = getAccessibleUserIds(userAgentId);
            if (!accessibleUserIds.contains(log.getUserId())) {
                throw new RuntimeException("无权访问该日志详情");
            }
        }
        
        return log;
    }

    /**
     * 分页查询操作日志
     * 
     * @param username 操作用户名
     * @param module 操作模块
     * @param operationType 操作类型
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     * @deprecated 使用 getLogPageWithPermission 替代，以支持数据隔离
     */
    @Deprecated
    public PageResult<OperationLog> getLogPage(String username, String module, String operationType,
                                                Integer status, LocalDateTime startTime, LocalDateTime endTime,
                                                Integer current, Integer size) {
        // 默认调用带权限的方法，使用超级管理员权限（无数据隔离）
        return getLogPageWithPermission(username, module, operationType, status, startTime, endTime, 
            current, size, "ADMIN", null);
    }

    /**
     * 保存操作日志
     * 
     * @param userId 操作用户ID
     * @param username 操作用户名
     * @param operationType 操作类型
     * @param module 操作模块
     * @param description 操作描述
     * @param targetId 操作对象ID
     * @param targetName 操作对象名称
     * @param ipAddress IP地址
     * @param status 状态
     * @param errorMsg 错误信息
     */
    @Transactional
    public void saveLog(Long userId, String username, String operationType, String module,
                         String description, Long targetId, String targetName,
                         String ipAddress, Integer status, String errorMsg) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setOperationType(operationType);
        log.setModule(module);
        log.setDescription(description);
        log.setTargetId(targetId);
        log.setTargetName(targetName);
        log.setIpAddress(ipAddress);
        log.setStatus(status);
        log.setErrorMsg(errorMsg);
        
        operationLogRepository.save(log);
    }

    /**
     * 根据ID查询日志详情
     * 
     * @param id 日志ID
     * @return 日志详情
     */
    public OperationLog getLogById(Long id) {
        return operationLogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("日志不存在"));
    }

    /**
     * 批量删除指定时间之前的日志
     * 
     * @param time 时间阈值
     */
    @Transactional
    public void deleteLogsBefore(LocalDateTime time) {
        operationLogRepository.deleteByCreateTimeBefore(time);
    }

    /**
     * 获取所有可用的模块列表
     * 
     * @return 模块列表
     */
    public List<String> getAllModules() {
        List<String> modules = new ArrayList<>();
        modules.add("AUTH");
        modules.add("AGENT");
        modules.add("USER");
        modules.add("CONFIG");
        modules.add("DEVICE");
        return modules;
    }

    /**
     * 获取所有可用的操作类型列表
     * 
     * @return 操作类型列表
     */
    public List<String> getAllOperationTypes() {
        List<String> types = new ArrayList<>();
        types.add("LOGIN");
        types.add("LOGOUT");
        types.add("CREATE");
        types.add("UPDATE");
        types.add("DELETE");
        types.add("QUERY");
        return types;
    }
}
