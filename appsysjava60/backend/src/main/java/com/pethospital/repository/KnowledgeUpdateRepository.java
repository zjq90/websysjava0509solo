package com.pethospital.repository;

import com.pethospital.entity.KnowledgeUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 知识库更新记录数据访问层
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface KnowledgeUpdateRepository extends JpaRepository<KnowledgeUpdate, Long> {
    
    /**
     * 根据类型查询更新记录
     * 
     * @param type 类型
     * @return 更新记录列表
     */
    List<KnowledgeUpdate> findByTypeAndStatusOrderByCreateTimeDesc(String type, Integer status);
    
    /**
     * 查询所有已发布的更新记录
     * 
     * @param status 状态
     * @return 更新记录列表
     */
    List<KnowledgeUpdate> findByStatusOrderByCreateTimeDesc(Integer status);
}
