package com.websys.repository;

import com.websys.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 代理商Repository接口
 * 
 * @author websys
 * @version 1.0.0
 */
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long>, JpaSpecificationExecutor<Agent> {

    /**
     * 根据代理商编码查询
     * 
     * @param agentCode 代理商编码
     * @return 代理商信息
     */
    Optional<Agent> findByAgentCode(String agentCode);

    /**
     * 根据父级代理商ID查询子代理商列表
     * 
     * @param parentId 父级代理商ID
     * @return 子代理商列表
     */
    List<Agent> findByParentId(Long parentId);

    /**
     * 根据级别查询代理商列表
     * 
     * @param level 代理商级别
     * @return 代理商列表
     */
    List<Agent> findByLevel(Integer level);

    /**
     * 检查代理商编码是否存在
     * 
     * @param agentCode 代理商编码
     * @return 是否存在
     */
    boolean existsByAgentCode(String agentCode);

    /**
     * 检查代理商编码是否存在（排除指定ID）
     * 
     * @param agentCode 代理商编码
     * @param id 排除的ID
     * @return 是否存在
     */
    boolean existsByAgentCodeAndIdNot(String agentCode, Long id);
}
