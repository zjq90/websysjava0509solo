package com.websys.service;

import com.websys.common.PageResult;
import com.websys.entity.Agent;
import com.websys.repository.AgentRepository;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 代理商服务类
 * 
 * @author websys
 * @version 1.0.0
 */
@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 分页查询代理商列表（支持数据隔离）
     * 
     * @param agentName 代理商名称（模糊查询）
     * @param agentCode 代理商编码（模糊查询）
     * @param parentId 父级代理商ID
     * @param level 代理商级别
     * @param status 状态
     * @param current 当前页
     * @param size 每页大小
     * @param roleType 角色类型（用于数据隔离）
     * @param userAgentId 用户所属代理商ID（用于数据隔离）
     * @return 分页结果
     */
    public PageResult<Agent> getAgentPage(String agentName, String agentCode, Long parentId, 
                                          Integer level, Integer status, Integer current, Integer size,
                                          String roleType, Long userAgentId) {
        Pageable pageable = PageRequest.of(current - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<Agent> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(agentName)) {
                predicates.add(criteriaBuilder.like(root.get("agentName"), "%" + agentName + "%"));
            }

            if (StringUtils.hasText(agentCode)) {
                predicates.add(criteriaBuilder.like(root.get("agentCode"), "%" + agentCode + "%"));
            }

            if (parentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("parentId"), parentId));
            }

            if (level != null) {
                predicates.add(criteriaBuilder.equal(root.get("level"), level));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 数据隔离：非超级管理员只能看到自己所属的代理商及其下级
            if (!"ADMIN".equals(roleType) && userAgentId != null) {
                List<Long> accessibleAgentIds = getAllDescendantIds(userAgentId);
                predicates.add(root.get("id").in(accessibleAgentIds));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Agent> page = agentRepository.findAll(spec, pageable);
        return PageResult.of(page.getContent(), page.getTotalElements(), current, size);
    }

    /**
     * 根据ID查询代理商（支持数据隔离校验）
     * 
     * @param id 代理商ID
     * @param roleType 角色类型
     * @param userAgentId 用户所属代理商ID
     * @return 代理商信息
     */
    public Agent getAgentByIdWithPermission(Long id, String roleType, Long userAgentId) {
        Agent agent = agentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("代理商不存在"));
        
        // 数据隔离校验：非超级管理员只能访问自己有权限的代理商
        if (!"ADMIN".equals(roleType) && userAgentId != null) {
            List<Long> accessibleAgentIds = getAllDescendantIds(userAgentId);
            if (!accessibleAgentIds.contains(id)) {
                throw new RuntimeException("无权访问该代理商信息");
            }
        }
        
        return agent;
    }

    /**
     * 获取顶级代理商（支持数据隔离）
     * 
     * @param roleType 角色类型
     * @param userAgentId 用户所属代理商ID
     * @return 代理商列表
     */
    public List<Agent> getTopLevelAgentsWithPermission(String roleType, Long userAgentId) {
        // 如果是超级管理员，返回所有一级代理商
        if ("ADMIN".equals(roleType) || userAgentId == null) {
            return agentRepository.findByLevel(1);
        }
        
        // 非超级管理员，只返回用户所属的代理商（作为父级选择时只能选自己或下级，但父级选择通常只在创建时使用）
        // 这里特殊处理：代理商创建时如果是非管理员，只能创建自己的下级，所以返回自己
        Agent selfAgent = agentRepository.findById(userAgentId).orElse(null);
        if (selfAgent != null) {
            return Collections.singletonList(selfAgent);
        }
        return new ArrayList<>();
    }

    /**
     * 获取子级代理商（支持数据隔离）
     * 
     * @param parentId 父级代理商ID
     * @param roleType 角色类型
     * @param userAgentId 用户所属代理商ID
     * @return 子级代理商列表
     */
    public List<Agent> getChildAgentsWithPermission(Long parentId, String roleType, Long userAgentId) {
        // 数据隔离校验
        if (!"ADMIN".equals(roleType) && userAgentId != null) {
            List<Long> accessibleAgentIds = getAllDescendantIds(userAgentId);
            if (!accessibleAgentIds.contains(parentId)) {
                throw new RuntimeException("无权访问该代理商的子级信息");
            }
        }
        return agentRepository.findByParentId(parentId);
    }

    /**
     * 根据ID查询代理商
     * 
     * @param id 代理商ID
     * @return 代理商信息
     */
    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("代理商不存在"));
    }

    /**
     * 创建代理商
     * 
     * @param agent 代理商信息
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @return 创建后的代理商
     */
    @Transactional
    public Agent createAgent(Agent agent, Long operatorId, String operatorName) {
        if (agentRepository.existsByAgentCode(agent.getAgentCode())) {
            throw new RuntimeException("代理商编码已存在");
        }

        if (agent.getParentId() != null && agent.getParentId() > 0) {
            Agent parentAgent = getAgentById(agent.getParentId());
            agent.setLevel(parentAgent.getLevel() + 1);
        } else {
            agent.setLevel(1);
        }

        if (agent.getStatus() == null) {
            agent.setStatus(1);
        }

        Agent savedAgent = agentRepository.save(agent);

        operationLogService.saveLog(
            operatorId,
            operatorName,
            "CREATE",
            "AGENT",
            "创建代理商：" + savedAgent.getAgentName(),
            savedAgent.getId(),
            savedAgent.getAgentName(),
            null,
            1,
            null
        );

        return savedAgent;
    }

    /**
     * 更新代理商
     * 
     * @param agent 代理商信息
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @return 更新后的代理商
     */
    @Transactional
    public Agent updateAgent(Agent agent, Long operatorId, String operatorName) {
        Agent existingAgent = getAgentById(agent.getId());

        if (agentRepository.existsByAgentCodeAndIdNot(agent.getAgentCode(), agent.getId())) {
            throw new RuntimeException("代理商编码已存在");
        }

        existingAgent.setAgentName(agent.getAgentName());
        existingAgent.setAgentCode(agent.getAgentCode());
        existingAgent.setContactName(agent.getContactName());
        existingAgent.setContactPhone(agent.getContactPhone());
        existingAgent.setContactEmail(agent.getContactEmail());
        existingAgent.setAddress(agent.getAddress());
        existingAgent.setStatus(agent.getStatus());

        Agent savedAgent = agentRepository.save(existingAgent);

        operationLogService.saveLog(
            operatorId,
            operatorName,
            "UPDATE",
            "AGENT",
            "更新代理商：" + savedAgent.getAgentName(),
            savedAgent.getId(),
            savedAgent.getAgentName(),
            null,
            1,
            null
        );

        return savedAgent;
    }

    /**
     * 删除代理商
     * 
     * @param id 代理商ID
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     */
    @Transactional
    public void deleteAgent(Long id, Long operatorId, String operatorName) {
        Agent agent = getAgentById(id);

        List<Agent> children = agentRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子级代理商，无法删除");
        }

        agentRepository.deleteById(id);

        operationLogService.saveLog(
            operatorId,
            operatorName,
            "DELETE",
            "AGENT",
            "删除代理商：" + agent.getAgentName(),
            agent.getId(),
            agent.getAgentName(),
            null,
            1,
            null
        );
    }

    /**
     * 获取所有顶级代理商
     * 
     * @return 顶级代理商列表
     */
    public List<Agent> getTopLevelAgents() {
        return agentRepository.findByLevel(1);
    }

    /**
     * 获取指定代理商的子级代理商
     * 
     * @param parentId 父级代理商ID
     * @return 子级代理商列表
     */
    public List<Agent> getChildAgents(Long parentId) {
        return agentRepository.findByParentId(parentId);
    }

    /**
     * 获取代理商及其所有下级代理商的ID列表
     * 
     * @param agentId 代理商ID
     * @return ID列表
     */
    public List<Long> getAllDescendantIds(Long agentId) {
        List<Long> ids = new ArrayList<>();
        collectDescendantIds(agentId, ids);
        return ids;
    }

    /**
     * 递归收集所有下级代理商ID
     * 
     * @param agentId 代理商ID
     * @param ids ID列表
     */
    private void collectDescendantIds(Long agentId, List<Long> ids) {
        ids.add(agentId);
        List<Agent> children = agentRepository.findByParentId(agentId);
        for (Agent child : children) {
            collectDescendantIds(child.getId(), ids);
        }
    }
}
